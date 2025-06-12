package com.powercess.printersystem.printersystem.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
public interface DocumentService {
    Map<String, Object> uploadDocument(Long userId, MultipartFile file);
    void downloadDocumentById(Long documentId, HttpServletResponse response);
    Map<String, Object> listDocumentsByUserId(Long userId, int page, int size);
    void deleteDocumentById(Long documentId);
}