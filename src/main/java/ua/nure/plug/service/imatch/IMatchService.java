package ua.nure.plug.service.imatch;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.Text;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.TextService;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IMatchService {

    private static final double REMOVE_PERCENT = 0.3 * 2;
    private static final int ADDITIONAL_DICTIONARIES_COUNT = 5;
    private static final int DICTIONARY_REMOVE_PERCENT = 30;
    private static final int INTERSECTION_POWER = 60;

    private String hash = "";
    private Set<String> lDict = new HashSet<>();
    private List<IdfWord> bDict = new ArrayList<>();
    private List<Set<String>> dictionaries = new ArrayList<>();

    @Autowired
    private TextService textService;
    @Autowired
    private TextTokenizer tokenizer;
    @Autowired
    private HashService hashService;
    @Autowired
    private DocumentService documentService;

    private Map<String, String> signatures = new HashMap<>();
    private Map<String, Set<String>> additionalSignatures = new HashMap<>();

    private Map<String, Set<String>> invertedIndex = new HashMap<>();


    //===========================================================================

    public double similarityMulti(String documentId1, String documentId2) {
        if (dictionaries.size() == 0) {
            List<Document> documents = documentService.getAll();
            createDoc();

            for (Document document : documents) {
                Set<String> words = getDocumentWords(document.getId());
                String signature = iMatchSignature(document.getId());
                String additionalSignature = calcBSignature(words);
                additionalSignatures.put(document.getId(), calcSignatures(words));
                additionalSignatures.get(document.getId()).add(signature);
                if (!signature.equals(additionalSignature)) {
                    additionalSignatures.get(document.getId()).add(additionalSignature);
                }
            }
//            createInvertedIndex();
        }

        Set<String> signatures1 = new HashSet<>(additionalSignatures.get(documentId1));
        Set<String> signatures2 = new HashSet<>(additionalSignatures.get(documentId2));

        signatures1.retainAll(signatures2);

        return signatures1.size() > 0 ? 1 : 0;
    }

    private void createInvertedIndex() {
        for (Map.Entry<String, Set<String>> entry : additionalSignatures.entrySet()) {
            for (String signature : entry.getValue()) {
                if (!invertedIndex.containsKey(signature)) {
                    invertedIndex.put(signature, new HashSet<>());
                }
                invertedIndex.get(signature).add(entry.getKey());
            }
        }
    }

    private Set<String> getDocumentWords(String documentId) {
        Text text = textService.getByDocumentId(documentId);
        return new HashSet<>(tokenizer.tokenizeWords(text.getNormalized()));
    }

    private Set<String> calcSignatures(Set<String> words) {
        return dictionaries.stream()
                .map(dictionary -> calcSignature(dictionary, words))
                .collect(Collectors.toSet());
    }

    private String calcSignature(Set<String> dictionary, Set<String> words) {
        Set<String> intersection = new HashSet<>(words);
        intersection.retainAll(dictionary);

        return getJoinedSha1Signature(intersection);
    }

    private String calcBSignature(Set<String> words) {
        Set<String> intersection = new HashSet<>(words);
        intersection.retainAll(lDict);

        if (intersection.size() <= INTERSECTION_POWER) {
            for (IdfWord aBDict : bDict) {
                String word = aBDict.getWord();
                if (words.contains(word)) {
                    intersection.add(word);
                    if (intersection.size() == INTERSECTION_POWER) {
                        break;
                    }
                }
            }
        }

        return getJoinedSha1Signature(intersection);
    }

    private String getJoinedSha1Signature(Set<String> intersection) {
        List<String> sorted = new ArrayList<>(intersection);
        sorted.sort(String::compareTo);

        String joined = sorted.stream().collect(Collectors.joining());
        return hashService.sha1(joined);
    }

    //===========================================================================

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
        Set<String> words = getDocumentWords(documentId);
        return calcSignature(lDict, words);
    }

    private void createDoc() {
        String joined = textService.getAll().stream()
                .map(Text::getDocument)
                .sorted()
                .collect(Collectors.joining());
        String docsHash = hashService.md5(joined);

        if (lDict.size() == 0 || dictionaries.size() == 0 || !hash.equals(docsHash)) {
            createLBDictionaries();
            createAdditionalDictionaries();
            hash = docsHash;
        }
    }



    private void createAdditionalDictionaries() {
        int wordsToRemove = (lDict.size() * DICTIONARY_REMOVE_PERCENT) / 100;
        for (int i = 0; i < ADDITIONAL_DICTIONARIES_COUNT; i++) {
            List<String> list = new ArrayList<>(lDict);
            Collections.shuffle(list);
            Set<String> additionalDictionary = new HashSet<>(list.subList(wordsToRemove, list.size()));
            dictionaries.add(additionalDictionary);
        }
    }

    private void createLBDictionaries() {
        List<Text> texts = textService.getAll();
        Map<String, Integer> wordsInEachDocument = wordsInEachDocument(texts);
        Map<String, Double> idf = calcIdf(wordsInEachDocument, texts.size());
        Set<Double> frequences = new HashSet<>(idf.values());
        int side = (int) (frequences.size() * REMOVE_PERCENT) / 2;

        List<Double> all = new ArrayList<>(frequences);
        all.sort((o1, o2) -> o2.compareTo(o1));
        List<Double> middle = all.subList(side, all.size() - side);
        List<Double> high = all.subList(0, side);

        lDict = idf.entrySet().stream()
                .filter(entry -> middle.contains(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        bDict = idf.entrySet().stream()
                .filter(entry -> high.contains(entry.getValue()))
                .map(entry -> new IdfWord(entry.getKey(), entry.getValue()))
                .sorted((o1, o2) -> o1.getIdf().compareTo(o2.getIdf()))
                .collect(Collectors.toList());
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class Signature {
        private String signature;
        private String additionalSignature;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class IdfWord {
        private String word;
        private Double idf;
    }

}
