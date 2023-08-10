package com.DataExtractor.SparkyFinance.SparkyFinance.Services;

import com.DataExtractor.SparkyFinance.SparkyFinance.Interfaces.MongoFileRepository;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.FileEntity;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.ResponseMessage;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileServiceMongo {

    @Value("${upload.path}") // Specify this in your application.properties
    private String uploadPath;
    private final MongoFileRepository mongoFileRepository;

    private final GridFsTemplate gridFsTemplate;


    private final GridFsOperations operations;

    private final MongoTemplate mongoTemplate;

    public FileServiceMongo(MongoFileRepository mongoFileRepository, GridFsOperations operations, GridFsTemplate gridFsTemplate, MongoTemplate mongoTemplate) {
        this.mongoFileRepository = mongoFileRepository;
        this.operations = operations;
        this.gridFsTemplate = gridFsTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    public FileEntity saveFile(MultipartFile file) throws IOException {

        FileEntity fileDocument = new FileEntity();
        fileDocument.setFileName(file.getOriginalFilename());
        fileDocument.setContentType(file.getContentType());
        fileDocument.setSize(file.getSize());
        fileDocument.setData(file.getBytes());
        return mongoFileRepository.save(fileDocument);


    }

    public FileEntity getFileById(String fileId) {
        return mongoFileRepository.findById(fileId).orElse(null);
    }

    public boolean getFileByName(String filename){

        FileEntity file = mongoFileRepository.getFileEntitiesByFileName(filename);
        return file != null;

    }

    public FileEntity getFileEntitiesByFileName(String filename) {
        return mongoFileRepository.getFileEntitiesByFileName(filename);
    }

    public boolean uploadFile(String file) throws IOException {


       var multipartFile = createMultipartFileFromPath(file);

        if (file.isEmpty()) {
            return false;
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath + File.separator +"Scaned_"+multipartFile.getOriginalFilename());
            Files.write(path, bytes);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public  MultipartFile createMultipartFileFromPath(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);

        return new MockMultipartFile(file.getName(), inputStream);
    }



}