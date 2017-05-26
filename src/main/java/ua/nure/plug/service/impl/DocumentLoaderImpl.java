package ua.nure.plug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.DocumentExtractor;
import ua.nure.plug.service.DocumentLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentLoaderImpl implements DocumentLoader {

    @Autowired
    private DocumentExtractor documentExtractor;

    @Override
    public Map<String, String> loadFromDirectory(String directory) {
        File folder = new File(directory);
        File[] files = folder.listFiles();
        Map<String, String> documents = new HashMap<>();
        if (files == null) {
            return documents;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            String text = documentExtractor.extractText(file);
            if (text.length() == 0) {
                continue;
            }
            documents.put(file.getName(), text);
        }
        return documents;
    }

}
