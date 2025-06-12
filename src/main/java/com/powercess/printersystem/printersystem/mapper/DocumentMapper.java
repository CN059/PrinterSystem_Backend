package com.powercess.printersystem.printersystem.mapper;

import com.powercess.printersystem.printersystem.model.Document;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocumentMapper {
    List<Document> selectByUserId(Long userId);
    Document selectById(Long id);
    void insert(Document document);
    void updateById(Document document);
}