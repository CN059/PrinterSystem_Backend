package com.powercess.printersystem.printersystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.service.DocumentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @PostMapping("/upload")
    @SaCheckLogin
    public ResponseResult<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
        Long userId = StpUtil.getLoginIdAsLong();
        Map<String, Object> result = documentService.uploadDocument(userId, file);
        return ResponseResult.success(result);
    }
    @GetMapping("/download/{documentId}")
    @SaCheckLogin
    public void download(@PathVariable Long documentId, HttpServletResponse response) {
        documentService.downloadDocumentById(documentId, response);
    }
    @GetMapping("/list")
    @SaCheckLogin
    public ResponseResult<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = StpUtil.getLoginIdAsLong();
        Map<String, Object> result = documentService.listDocumentsByUserId(userId, page, size);
        return ResponseResult.success(result);
    }
    @DeleteMapping("/delete/{documentId}")
    @SaCheckLogin
    public ResponseResult<Void> delete(@PathVariable Long documentId) {
        documentService.deleteDocumentById(documentId);
        return ResponseResult.success(null);
    }
}