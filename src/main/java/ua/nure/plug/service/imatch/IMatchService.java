package ua.nure.plug.service.imatch;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.Text;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.TextService;
import ua.nure.plug.service.text.TextTokenizer;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IMatchService {

    private static final double REMOVE_PERCENT = 0.5;

    private String hash = "";
    private Set<String> lDict = new HashSet<>();

    @Autowired
    private TextService textService;
    @Autowired
    private TextTokenizer tokenizer;
    @Autowired
    private HashService hashService;
    @Autowired
    private DocumentService documentService;

    private Map<String, String> signatures = new HashMap<>();


    public double similarity(String documentId1, String documentId2) {
        if (signatures.size() == 0) {
            List<Document> documents = documentService.getAll();
            for (Document document : documents) {
                signatures.put(document.getId(), iMatchSignature(document.getId()));
            }
        }

        boolean equals = signatures.get(documentId1).equals(signatures.get(documentId2));
        return equals ? 1 : 0;
    }

    public Map<String, Set<String>> iMatchSignatureMatches() {
        List<Document> documents = documentService.getAll();
        Map<String, String> signatures = new HashMap<>();
        for (Document document : documents) {
            signatures.put(document.getId(), iMatchSignature(document.getId()));
        }

        Map<String, Set<String>> docs = new HashMap<>();
        for (Map.Entry<String, String> entry : signatures.entrySet()) {
            if (!docs.containsKey(entry.getValue())) {
                Set<String> set = new HashSet<>();
                docs.put(entry.getValue(), set);
            }
            docs.get(entry.getValue()).add(entry.getKey());
        }
        return docs;
    }

    public String iMatchSignature(String documentId) {
        createDoc();

        Text text = textService.getByDocumentId(documentId);
        Set<String> words = new HashSet<>(tokenizer.tokenizeWords(text.getNormalized()));

        String signature = calcSignature(words);
        return signature;
    }

    private void createDoc() {
        String joined = textService.getAll().stream()
                .map(Text::getDocument)
                .sorted()
                .collect(Collectors.joining());
        String docsHash = hashService.md5(joined);

        if (lDict == null || !hash.equals(docsHash)) {
            createLDictionary();
            hash = docsHash;
        }
    }

    private String calcSignature(Set<String> words) {
        words.retainAll(lDict);
        List<String> sorted = new ArrayList<>(words);
        sorted.sort(String::compareTo);
        String joined = sorted.stream().collect(Collectors.joining());
        return hashService.sha1(joined);
    }

    private void createLDictionary() {
        List<Text> texts = textService.getAll();
        Map<String, Integer> wordsInEachDocument = wordsInEachDocument(texts);
        Map<String, Double> idf = calcIdf(wordsInEachDocument, texts.size());
        Set<Double> frequences = new HashSet<>(idf.values());
        int side = (int) (frequences.size() * REMOVE_PERCENT) / 2;

        List<Double> all = new ArrayList<>(frequences);
        all.sort((o1, o2) -> o2.compareTo(o1));
        List<Double> middle = all.subList(side, all.size() - side);

        lDict = idf.entrySet().stream()
                .filter(entry -> middle.contains(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private Map<String, Double> calcIdf(Map<String, Integer> wordsInEachDocument, int documentsCount) {
        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wordsInEachDocument.entrySet()) {
            idf.put(entry.getKey(), Math.log(1.0 * documentsCount / entry.getValue()));
        }

        return idf.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2.compareTo(o1)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<String, Integer> wordsInEachDocument(List<Text> texts) {
        Map<String, Integer> dict = new HashMap<>();
        for (Text text : texts) {
            Set<String> words = new HashSet<>(tokenizer.tokenizeWords(text.getNormalized()));
            for (String word : words) {
                if (!dict.containsKey(word)) {
                    dict.put(word, 0);
                }
                dict.put(word, dict.get(word) + 1);
            }
        }
        return dict;
    }

}
