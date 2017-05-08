package ua.nure.plug.init;

import lombok.extern.log4j.Log4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.nure.plug.model.elastic.Word;
import ua.nure.plug.repository.elastic.WordRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
@Component
public class ElasticSearchInit {

    @Value("${elastic.init.clearDictionaries:#{false}}")
    private boolean isClearDictionaries;

    @Value("${elastic.init.dictionaryZipRu:#{null}}")
    private String dictionaryZipFileRu;

    @Value("${elastic.init.dictionaryZipUa:#{null}}")
    private String dictionaryZipFileUa;

    @Autowired
    private WordRepository wordRepository;

    @PostConstruct
    public void initDictionaries() {
        clearElasticStorage();
        initDictionary(dictionaryZipFileRu, "ru");
        initDictionary(dictionaryZipFileUa, "ua");
    }

    private void clearElasticStorage() {
        if (isClearDictionaries) {
            wordRepository.deleteAll();
            log.info("All dictionaries was cleared.");
        }
    }

    private void initDictionary(String zipFileName, String lang) {
        List<Word> words = new ArrayList<>();
        Scanner scanner;
        Word word = new Word(lang);
        if (zipFileName != null && (scanner = getScanner(zipFileName)) != null) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("  ")) {
                    word = new Word(lang);
                    words.add(word);
                    String noun = getWord(line);
                    word.setNoun(noun);
                    word.addForm(noun);
                } else {
                    word.addForm(getWord(line));
                }
            }
            wordRepository.save(words);
            log.info("Dictionary for '" + lang + "' has been uploaded.");
        }
    }

    private static String getWord(String line) {
        return line.trim().split(" ")[0];
    }

    private Scanner getScanner(String zipFileName) {
        try {
            ZipFile zipFile = new ZipFile(getFile(zipFileName));
            ZipArchiveEntry entry = zipFile.getEntries().nextElement();
            return new Scanner(zipFile.getInputStream(entry), "UTF-8");
        } catch (IOException e) {
            log.error("Can't unzip dictionary.");
        }
        return null;
    }

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

}
