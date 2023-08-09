package com.DataExtractor.SparkyFinance.SparkyFinance.Interfaces;


import com.DataExtractor.SparkyFinance.SparkyFinance.Models.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFileRepository extends MongoRepository<FileEntity, String> {

    public FileEntity getFileEntitiesByFileName(String filename);

}
