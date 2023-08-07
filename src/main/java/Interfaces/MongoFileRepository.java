package Interfaces;


import Models.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFileRepository extends MongoRepository<FileEntity, String> { }
