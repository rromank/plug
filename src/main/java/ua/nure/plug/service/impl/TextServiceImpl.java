package ua.nure.plug.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.elastic.Text;
import ua.nure.plug.repository.elastic.TextRepository;
import ua.nure.plug.service.TextService;

import java.util.List;

@Service
public class TextServiceImpl implements TextService {

    @Autowired
    private TextRepository textRepository;

    @Override
    public void create(Text text) {
        textRepository.save(text);
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        Text text = textRepository.findByDocument(documentId);
        if (text != null) {
            textRepository.delete(text);
        }
    }

    @Override
    public void deleteAll() {
        textRepository.deleteAll();
    }

    @Override
    public Text getByDocumentId(String documentId) {
        return textRepository.findByDocument(documentId);
    }

}
