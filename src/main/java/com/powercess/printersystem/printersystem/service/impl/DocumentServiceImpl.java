package com.powercess.printersystem.printersystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.exception.BusinessException;
import com.powercess.printersystem.printersystem.mapper.DocumentMapper;
import com.powercess.printersystem.printersystem.model.Document;
import com.powercess.printersystem.printersystem.service.DocumentService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final String DOCUMENTS_DIR = "Documents";
    private final DocumentMapper documentMapper;
    @Autowired
    public DocumentServiceImpl(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }
    private boolean isPdfFile(String filename) {
        if (filename == null) return false;
        return filename.toLowerCase().endsWith(".pdf");
    }
    private File getStorageDirectory() {
        String rootDir = System.getProperty("user.dir");
        return new File(rootDir, DOCUMENTS_DIR);
    }
    private File getMonthlyDirectory() {
        String subDirName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        File baseDir = getStorageDirectory();
        if (!baseDir.exists()) baseDir.mkdirs();
        return new File(baseDir, subDirName);
    }
    private String generateUniqueFileName() {
        return UUID.randomUUID() + ".pdf";
    }
    private String getRelativePath(File file) {
        String rootDir = System.getProperty("user.dir");
        String path = file.getAbsolutePath().replace(rootDir, "");
        return path.replace("\\", "/");
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> uploadDocument(Long userId, MultipartFile file) {
        if (!isPdfFile(file.getOriginalFilename())) {
            throw new BusinessException(400, "仅支持 PDF 文件");
        }
        try {
            // 1. 创建存储目录
            File monthDir = getMonthlyDirectory();
            if (!monthDir.exists()) monthDir.mkdirs();
            // 2. 生成唯一文件名并保存文件
            String uniqueFileName = generateUniqueFileName();
            File destFile = new File(monthDir, uniqueFileName);
            file.transferTo(destFile);
            // 3. 使用 PDFBox 读取 PDF 页数
            int pageCount;
            try (PDDocument document = Loader.loadPDF(destFile)) {
                pageCount = document.getNumberOfPages();
            }
            // 4. 构建 Document 对象并插入数据库
            Document dbDocument = new Document();
            dbDocument.setUserId(userId);
            dbDocument.setFileName(file.getOriginalFilename());
            dbDocument.setFilePath(getRelativePath(destFile));
            dbDocument.setPageCount(pageCount); // ✅ 正确设置页数
            dbDocument.setUploadedAt(LocalDateTime.now());
            dbDocument.setIsPrinted(false);
            dbDocument.setIsDeleted(false);
            documentMapper.insert(dbDocument);
            // 5. 返回响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("id", dbDocument.getId());
            data.put("fileName", dbDocument.getFileName());
            data.put("filePath", dbDocument.getFilePath());
            data.put("pageCount", dbDocument.getPageCount());
            data.put("uploadedAt", dbDocument.getUploadedAt());
            return data;
        } catch (IOException e) {
            throw new BusinessException(500, "文件上传失败: " + e.getMessage());
        }
    }
    @Override
    public void downloadDocumentById(Long documentId, HttpServletResponse response) {
        Document document = documentMapper.selectById(documentId);
        if (document == null) {
            throw new BusinessException(404, "文档未找到");
        }
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!document.getUserId().equals(currentUserId)) {
            throw new BusinessException(403, "无权访问他人上传的文档");
        }
        String rootDir = System.getProperty("user.dir");
        String fullPath = rootDir + document.getFilePath();
        File file = new File(fullPath);
        if (!file.exists()) {
            throw new BusinessException(404, "文件不存在");
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"");
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new BusinessException(500, "文件下载失败: " + e.getMessage());
        }
    }
    @Override
    public Map<String, Object> listDocumentsByUserId(Long userId, int page, int size) {
        List<Document> documents = documentMapper.selectByUserId(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Document d : documents) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", d.getId());
            item.put("fileName", d.getFileName());
            item.put("filePath", d.getFilePath());
            item.put("pageCount", d.getPageCount());
            item.put("uploadedAt", d.getUploadedAt());
            list.add(item);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", documents.size());
        return data;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentById(Long documentId) {
        Document document = documentMapper.selectById(documentId);
        if (document == null) {
            throw new BusinessException(404, "文档未找到");
        }
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!document.getUserId().equals(currentUserId)) {
            throw new BusinessException(403, "无权删除他人上传的文档");
        }
        String rootDir = System.getProperty("user.dir");
        String fullPath = rootDir + document.getFilePath();
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        document.setIsDeleted(true);
        documentMapper.updateById(document);
    }
}