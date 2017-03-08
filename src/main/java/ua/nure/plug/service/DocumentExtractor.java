package ua.nure.plug.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentExtractor {

    String extractText(MultipartFile file);

}
