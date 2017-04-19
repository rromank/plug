package ua.nure.plug.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.plug.dto.DocumentInfo;
import ua.nure.plug.model.Document;

@Component
public class DocumentConverter implements Converter<Document, DocumentInfo> {

    @Override
    public DocumentInfo convert(Document document) {
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setId(document.getId());
        documentInfo.setDate(document.getDate());
        documentInfo.setShinglesCount(document.getShingles().size());
        documentInfo.setWordsCount(document.getWords().size());
        return documentInfo;
    }

}
