package ua.nure.plug.service.impl;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.WordDocument;
import ua.nure.plug.service.DocumentExtractor;
import ua.nure.plug.service.WordsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DocumentExtractorImpl implements DocumentExtractor {

    private Pattern pattern = Pattern.compile("[а-яА-Яёєїі]{3,}");

    @Autowired
    private WordsService wordsService;

    @Override
    public WordDocument extract(String fileName) {
        WordDocument textDocument = new WordDocument();
        try (FileInputStream fis = FileUtils.openInputStream(new File(fileName))) {
            XWPFDocument xwpf = new XWPFDocument(fis);
            XWPFWordExtractor ex = new XWPFWordExtractor(xwpf);

            textDocument.setFullText(ex.getText());

            for (XWPFParagraph para : xwpf.getParagraphs()) {
                Matcher matcher = pattern.matcher(para.getText());
                while (matcher.find()) {
                    textDocument.addWord(matcher.group().toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillNouns(textDocument);
        return textDocument;
    }

    private void fillNouns(WordDocument document) {
        AtomicInteger i = new AtomicInteger(0);
        int wordsCount = document.getWords().size();
        List<String> nouns = document.getWords().parallelStream()
                .peek(s -> {
                    System.out.println(i.incrementAndGet() + " / " + wordsCount);
                })
                .map(word -> wordsService.getNoun(word, "ru"))
                .collect(Collectors.toList());

        List<NounWithForm> nounsWithForms = document.getWords().parallelStream()
                .peek(s -> {
                    System.out.println(i.incrementAndGet() + " / " + wordsCount);
                })
                .map(word -> new NounWithForm(wordsService.getNoun(word, "ru"), word))
                .collect(Collectors.toList());

        try {
            for (NounWithForm nounWithForm : nounsWithForms) {
                FileUtils.writeStringToFile(new File("src/main/resources/result.txt"),
                        nounWithForm.toString(), Charset.forName("utf-8"), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(nounsWithForms);

        document.setNouns(nouns);
    }

    @ToString
    @AllArgsConstructor
    private class NounWithForm {
        private String noun;
        private String form;
    }

}
