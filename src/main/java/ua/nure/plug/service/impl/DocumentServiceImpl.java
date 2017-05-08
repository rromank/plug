package ua.nure.plug.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.ShingleDocument;
import ua.nure.plug.model.elastic.Text;
import ua.nure.plug.repository.elastic.DocumentRepository;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.ShingleDocumentService;
import ua.nure.plug.service.TextService;
import ua.nure.plug.service.normalization.NormalizationService;
import ua.nure.plug.service.shingle.ShingleService;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private HashService hashService;
    @Autowired
    private ShingleService shingleService;
    @Autowired
    private TextService textService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ShingleDocumentService shingleDocumentService;
    @Autowired
    private NormalizationService normalizationService;

    @Override
    public Document getById(String id) {
        return documentRepository.findOne(id);
    }

    public Document getByText(String text) {
        String id = hashService.md5(text);
        return documentRepository.findOne(id);
    }

    @Override
    public Document createFrom(String filename, String text) {
        String documentId = hashService.md5(text);
        Document document = new Document();
        document.setId(documentId);
        document.setFilename(filename);
        document.setDate(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis()));

        String normalized = normalizationService.normalize(text);
        textService.create(new Text(documentId, text, normalized));
        shingleDocumentService.create(new ShingleDocument(documentId, shingleService.createShingles(normalized)));

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAll() {
        if (documentRepository.count() == 0) {
            return Lists.newArrayList();
        }
        return documentRepository.findAll().getContent();
    }

    @Override
    public void delete(String id) {
        documentRepository.delete(id);
        textService.deleteByDocumentId(id);
        shingleDocumentService.deleteByDocumentId(id);
    }

    @Override
    public void deleteAll() {
        documentRepository.deleteAll();
        textService.deleteAll();
        shingleDocumentService.deleteAll();
    }

}
