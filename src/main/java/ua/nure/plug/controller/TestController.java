package ua.nure.plug.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.nure.plug.model.ComplexSim;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.MinHashDocument;
import ua.nure.plug.model.elastic.ShingleDocument;
import ua.nure.plug.service.*;
import ua.nure.plug.service.imatch.IMatchService;
import ua.nure.plug.service.similarity.CosineSimilarity;
import ua.nure.plug.service.similarity.MinHashSimilarity;
import ua.nure.plug.service.similarity.ShingleSimilarity;
import ua.nure.plug.service.text.TextTokenizer;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DocumentLoader documentLoader;
    @Autowired
    private TextTokenizer textTokenizer;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ShingleDocumentService shingleDocumentService;
    @Autowired
    private MinHashDocumentService minHashDocumentService;
    @Autowired
    private ShingleSimilarity shingleSimilarity;
    @Autowired
    private MinHashSimilarity minHashSimilarity;
    @Autowired
    private IMatchService iMatchService;
    @Autowired
    private TextService textService;
    @Autowired
    private CosineSimilarity cosineSimilarity;

    @RequestMapping(value = "/cosine", method = RequestMethod.GET)
    public String cosine(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        String text1 = textService.getByDocumentId(documentId1).getNormalized();
        String text2 = textService.getByDocumentId(documentId2).getNormalized();

        return String.valueOf(cosineSimilarity.cosine(text1, text2));
    }

    private double  cosineById(String documentId1, String documentId2) {
        String text1 = textService.getByDocumentId(documentId1).getNormalized();
        String text2 = textService.getByDocumentId(documentId2).getNormalized();

        return cosineSimilarity.cosine(text1, text2);
    }

    @RequestMapping(value = "/shingle/jaccard", method = RequestMethod.GET)
    public String shinglesJaccard(@RequestParam("documentId1") String documentId1, @RequestParam("documentId2") String documentId2) {
        ShingleDocument shingleDocument1 = shingleDocumentService.getByDocumentId(documentId1);
        ShingleDocument shingleDocument2 = shingleDocumentService.getByDocumentId(documentId2);

        double coef = shingleSimilarity.jaccard(shingleDocument1, shingleDocument2);
        return String.valueOf(coef);
    }

    @RequestMapping(value = "/shingle/precision", method = RequestMethod.GET)
    public String benchmarkShingle() {
        long start = System.currentTimeMillis();
        final double T = 0.9;
        Set<Result> results = new HashSet<>();

        int cos = 0;
        int jac = 0;
        int i = 0;
        List<ShingleDocument> documents = shingleDocumentService.getAll();
        for (ShingleDocument document1 : documents) {
            System.out.println(i++);
            String documentId1 = document1.getDocument();
            for (ShingleDocument document2 : documents) {
                String documentId2 = document2.getDocument();
                if (documentId1.equals(documentId2)) {
                    continue;
                }
                Result result = new Result(documentId1, documentId2);
                if (results.contains(result)) {
                    continue;
                }
                results.add(result);

                if (cosineById(documentId1, documentId2) >= T) {
                    cos++;
                }
                if (shingleSimilarity.jaccard(document1, document2) >= T) {
                    jac++;
                }
            }
        }

        int inter = Math.min(jac, cos);
        double precision = (1.0 * inter / jac);
        double recall = (1.0 * inter / cos);
        double F = (2 * precision * recall) / (precision + recall);
        System.out.println("Precision: " + precision + ", recall: " + recall + ", F: " + F);


        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Elapsed time: " + elapsed + " ms.");

        return "Precision: " + precision + ", recall: " + recall + ", F: " + F;
    }

    @RequestMapping(value = "/minhash/precision", method = RequestMethod.GET)
    public String minhashPrecision() {
        final double T = 0.9;
        Set<Result> results = new HashSet<>();

        int cos = 0;
        int jac = 0;
        int i = 0;
        List<MinHashDocument> minHashDocuments = minHashDocumentService.getAll();
        for (MinHashDocument minHashDocument : minHashDocuments) {
            System.out.println(i++);
            String documentId1 = minHashDocument.getDocument();
            for (MinHashDocument minHashDocument2 : minHashDocuments) {
                String documentId2 = minHashDocument2.getDocument();
                if (documentId1.equals(documentId2)) {
                    continue;
                }
                Result result = new Result(documentId1, documentId2);
                if (results.contains(result)) {
                    continue;
                }
                results.add(result);

                if (cosineById(documentId1, documentId2) >= T) {
                    cos++;
                }
                if (minHashSimilarity.similarity(minHashDocument, minHashDocument2) >= T) {
                    jac++;
                }
            }
        }

        int inter = Math.min(jac, cos);
        double precision = (1.0 * inter / jac);
        double recall = (1.0 * inter / cos);
        double F = (2 * precision * recall) / (precision + recall);
        System.out.println("Precision: " + precision + ", recall: " + recall + ", F: " + F);

        return "Precision: " + precision + ", recall: " + recall + ", F: " + F;
    }

    @RequestMapping(value = "/imatch/precision", method = RequestMethod.GET)
    public String imatchPrecision() {
        Set<Result> results = new HashSet<>();
        Map<String, Double> precisionMap = new HashMap<>();
        Map<String, Double> recallMap = new HashMap<>();

        int i = -1;
        List<Document> documents = documentService.getAll();
        for (Document document : documents) {
            i++;
            System.out.println(i);
            int found = 0;
            int cos = 0;
            for (Document document2 : documents) {
                Result result = new Result(document.getId(), document2.getId());
                if (results.contains(result)) {
                    continue;
                }
                results.add(result);


                if (!document.getId().equals(document2.getId())) {
                    double jaccard = iMatchService.similarity(document.getId(), document2.getId());
                    double cosin = cosineById(document.getId(), document2.getId());

                    double t= 0.8;
                    if (cosin >= t) {
                        cos++;
                    }
                    if (jaccard >= t) {
                        found++;
                    }
                    int inter = Math.min(found, cos);

                    if (found != 0) {
                        precisionMap.put(document.getId(), 1.0 * inter / found);
                    }
                    if (cos != 0) {
                        recallMap.put(document.getId(), 1.0 * inter / cos);
                    }
                }
            }
//            System.out.println(precisionMap.get(document.getId()));
//            System.out.println(recallMap.get(document.getId()));
        }
        double precision = 0.0;
        for (Map.Entry<String, Double> entry : precisionMap.entrySet()) {
            precision += entry.getValue();
        }
        precision = precision / precisionMap.size();

        double recall = 0.0;
        for (Map.Entry<String, Double> entry : recallMap.entrySet()) {
            recall += entry.getValue();
        }
        recall = recall / recallMap.size();

        double F = (2 * precision * recall) / (precision + recall);

        return "Precision: " + precision + ", recall: " + recall + ", F: " + F;
    }

    @RequestMapping(value = "/imatch/multi-precision", method = RequestMethod.GET)
    public String imatchMultiPrecision() {
        Set<Result> results = new HashSet<>();
        Map<String, Double> precisionMap = new HashMap<>();
        Map<String, Double> recallMap = new HashMap<>();

        int i = -1;
        List<Document> documents = documentService.getAll();
        for (Document document : documents) {
            i++;
            System.out.println(i);
            int duplicateCount = 0;
            int foundCount = 0;
            for (Document document2 : documents) {
                if (document.getId().equals(document2.getId())) {
                    continue;
                }
                Result result = new Result(document.getId(), document2.getId());
                if (results.contains(result)) {
                    continue;
                }
                results.add(result);


                double cosin = cosineById(document.getId(), document2.getId());
                double jaccard = iMatchService.similarityMulti(document.getId(), document2.getId());

                double t= 0.9;
                if (cosin >= t) {
                    duplicateCount++;
                }
                if (jaccard >= t) {
                    foundCount++;
                }
            }

            int intersection = Math.min(foundCount, duplicateCount);
            if (duplicateCount != 0) {
                recallMap.put(document.getId(), 1.0 * intersection / duplicateCount);
            }
            if (foundCount != 0) {
                precisionMap.put(document.getId(), 1.0 * intersection / foundCount);
            }

        }
        double precision = 0.0;
        for (Map.Entry<String, Double> entry : precisionMap.entrySet()) {
            precision += entry.getValue();
        }
        precision = precision / precisionMap.size();

        double recall = 0.0;
        for (Map.Entry<String, Double> entry : recallMap.entrySet()) {
            recall += entry.getValue();
        }
        recall = recall / recallMap.size();

        double F = (2 * precision * recall) / (precision + recall);

        return "Precision: " + precision + ", recall: " + recall + ", F: " + F;
    }

    @RequestMapping(value = "/loadNonNormalized", method = RequestMethod.GET)
    public String loadNotNormalizedFiles(@RequestParam("directory") String directory) {
        Map<String, String> documents = documentLoader.loadFromDirectory(directory);
        List<String> allWords = new ArrayList<>();
        List<String> normalizedWords = new ArrayList<>();
        for (String document : documents.values()) {
            allWords.addAll(textTokenizer.tokenizeBySpace(document));
            normalizedWords.addAll(textTokenizer.tokenizeWords(document));
        }

        System.out.println(allWords.size());
        return String.valueOf("Not normalized words: " + allWords.size() + ". Normalized words: " + normalizedWords.size());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadDocuments(@RequestParam("directory") String directory,
                                  @RequestParam(value = "count", defaultValue = "-1") Integer count) {
        Map<String, String> texts = documentLoader.loadFromDirectory(directory);

        Map<Integer, Long> times = new HashMap<>();
        long start = System.currentTimeMillis();

        int i = 0;
        for (Map.Entry<String, String> entry : texts.entrySet()) {
            if (count == i) {
                break;
            }
            documentService.createFrom(entry.getKey(), entry.getValue());
            times.put(i, System.currentTimeMillis() - start);
            i++;
        }
        return printTime(times);
    }

    @RequestMapping(value = "/compare/shingle", method = RequestMethod.GET)
    public String compareAllDocumentsShingle() {
        List<ShingleDocument> shingleDocuments = shingleDocumentService.getAll();

        Map<Integer, Long> times = new HashMap<>();
        long now = System.currentTimeMillis();
        int i = 0;

        List<ComplexSim> complexSims = new ArrayList<>();
        for (ShingleDocument shingleDocument : shingleDocuments) {
            List<ShingleDocument> otherDocuments = shingleDocuments.stream()
                    .filter(shingleDocument1 -> !shingleDocument1.getId().equals(shingleDocument.getId()))
                    .collect(Collectors.toList());
            complexSims.add(shingleSimilarity.similarity(shingleDocument, otherDocuments));

            times.put(i, System.currentTimeMillis() - now);
            i++;
        }
        return String.valueOf(armean(times));
    }

    @RequestMapping(value = "/compare/minhash", method = RequestMethod.GET)
    public String compareAllDocumentsMinHash() {
        List<MinHashDocument> minHashDocuments = minHashDocumentService.getAll();

        Map<Integer, Long> times = new HashMap<>();
        long now = System.currentTimeMillis();
        int i = 0;

        Map<String, Map<String, Double>> sims = new HashMap<>();
        for (MinHashDocument minHashDocument : minHashDocuments) {
            List<MinHashDocument> otherDocuments = minHashDocuments.stream()
                    .filter(minHashDocument1 -> !minHashDocument1.getId().equals(minHashDocument.getId()))
                    .collect(Collectors.toList());
            sims.put(minHashDocument.getDocument(), minHashSimilarity.similarity(minHashDocument, otherDocuments));

            times.put(i, System.currentTimeMillis() - now);
            i++;
        }

        long withDups = sims.entrySet().stream()
                .filter(entry -> entry.getValue().entrySet().size() > 0)
                .count();

        return String.valueOf(armean(times));
    }

    @RequestMapping(value = "/compare/all/imatch", method = RequestMethod.GET)
//    public Map<String, Set<String>> iMatchAll() {
    public String iMatchAll() {
        long now = System.currentTimeMillis();
        Map<String, Set<String>> signatures = iMatchService.iMatchSignatureMatches();
        return String.valueOf(System.currentTimeMillis() - now);
//        return signatures;
    }

    @RequestMapping(value = "/compare/imatch", method = RequestMethod.GET)
    public String iMatch(@RequestParam(value = "documentId") String documentId) {
        String signature = iMatchService.iMatchSignature(documentId);

        return signature;
    }

    private String printTime(Map<Integer, Long> times) {
        return times.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
    }

    private long armean(Map<Integer, Long> times) {
        long allTime = 0;
        for (long time : times.values()) {
            allTime += time;
        }
        return allTime / times.size();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class Coeefficient {
        double coefficient;
        double cosine;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class Precision {
        double foundCount;
        double cosineCount;

    }

    private class Result {
        private Set<String> documents = new TreeSet<>();

        public Result(String document1, String document2) {
            documents.add(document1);
            documents.add(document2);
        }

        public void addDocument(String document) {
            documents.add(document);
        }

        public String getDocuments() {
            return documents.stream()
                    .collect(Collectors.joining("#"));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;

            return result.getDocuments().equals(getDocuments());
        }

        @Override
        public int hashCode() {
            return getDocuments().hashCode();
        }
    }

}
