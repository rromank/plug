package ua.nure.plug.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface DocumentExtractor {

    String extractText(File file);

    String extractText(MultipartFile file);

}
