package ua.nure.plug.service.impl;


import lombok.extern.log4j.Log4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ua.nure.plug.service.DocumentExtractor;

import java.io.InputStream;

@Log4j
@Component
public class DocumentExtractorImpl implements DocumentExtractor {

    @Override
    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument xwpf = new XWPFDocument(inputStream);
            XWPFWordExtractor wordExtractor = new XWPFWordExtractor(xwpf);
            return wordExtractor.getText().trim();
        } catch (Exception e) {
            log.error("Can't extract text from file.");
        }
        return "";
    }

}
