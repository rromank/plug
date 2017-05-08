package ua.nure.plug.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.nure.plug.dto.DocumentInfo;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.service.ShingleDocumentService;

@Component
public class DocumentConverter implements Converter<Document, DocumentInfo> {

    @Autowired
    private ShingleDocumentService shingleDocumentService;

    @Override
    public DocumentInfo convert(Document document) {
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setId(document.getId());
        documentInfo.setFilename(document.getFilename());
        documentInfo.setDate(document.getDate());

        int shinglesCount = shingleDocumentService.getByDocumentId(document.getId()).getShingles().size();
        documentInfo.setShinglesCount(shinglesCount);
        return documentInfo;
    }

}
