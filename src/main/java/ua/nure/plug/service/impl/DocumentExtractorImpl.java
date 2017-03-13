package ua.nure.plug.service.impl;


import lombok.extern.log4j.Log4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ua.nure.plug.service.DocumentExtractor;

import java.io.InputStream;
import java.util.regex.Pattern;

@Log4j
@Component
public class DocumentExtractorImpl implements DocumentExtractor {

    private Pattern pattern = Pattern.compile("[а-яА-Яёєїі\']{3,}");

    @Override
    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument xwpf = new XWPFDocument(inputStream);
            XWPFWordExtractor wordExtractor = new XWPFWordExtractor(xwpf);
            return wordExtractor.getText().trim().toLowerCase();
        } catch (Exception e) {
            log.error("Can't extract text from file.");
        }
        return "";
    }

}
