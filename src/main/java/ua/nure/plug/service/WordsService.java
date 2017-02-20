package ua.nure.plug.service;

public interface WordsService {

    void loadWords(String filePath, String lang);
    void deleteAll();
    String getNoun(String word, String lang);

}
