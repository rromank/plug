package ua.nure.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.nure.plug.converter.DocumentConverter;
import ua.nure.plug.dto.DocumentInfo;
import ua.nure.plug.dto.ResponseMessage;
import ua.nure.plug.dto.ResponseMessageType;
import ua.nure.plug.model.elastic.Document;
import ua.nure.plug.model.elastic.Text;
import ua.nure.plug.service.DocumentExtractor;
import ua.nure.plug.service.DocumentService;
import ua.nure.plug.service.TextService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private TextService textService;
    @Autowired
    private DocumentExtractor documentExtractor;
    @Autowired
    private DocumentConverter documentConverter;

    @RequestMapping(method = RequestMethod.GET)
    public List<DocumentInfo> getAll() {
        return documentService.getAll().stream()
                .map(documentConverter::convert)
                .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DocumentInfo getById(@PathVariable("id") String id) {
        Document document = documentService.getById(id);
        return documentConverter.convert(document);
    }

    @RequestMapping(value = "text/{id}", method = RequestMethod.GET)
    public DocumentInfo getTextById(@PathVariable("id") String id) {
        Document document = documentService.getById(id);
        Text text = textService.getByDocumentId(id);
        DocumentInfo dto = documentConverter.convert(document);
        dto.setText(text.getNormalized());

        return dto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseMessage uploadFile(@RequestParam("file") MultipartFile file) {
        String text = documentExtractor.extractText(file);
        if (documentService.getByText(text) != null) {
            return new ResponseMessage("Same document already exists.", ResponseMessageType.ERROR);
        }
        Document document = documentService.createFrom(file.getOriginalFilename(), text);
        return new ResponseMessage(document.getId(), ResponseMessageType.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        documentService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.DELETE)
    public void deleteAll() {
        documentService.deleteAll();
    }

}
