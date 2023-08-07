package com.DataExtractor.SparkyFinance.SparkyFinance.Services;

import com.DataExtractor.SparkyFinance.SparkyFinance.Interfaces.MongoFileRepository;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.FileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class FileServiceMongo {

    private final MongoFileRepository mongoFileRepository;

    public FileServiceMongo(MongoFileRepository mongoFileRepository) {
        this.mongoFileRepository = mongoFileRepository;
    }

    public String saveFile(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileContent(file.getBytes());
        fileEntity = mongoFileRepository.save(fileEntity);
        return fileEntity.getId();
    }

    public FileEntity getFileById(String fileId) {
        return mongoFileRepository.findById(fileId).orElse(null);
    }
}