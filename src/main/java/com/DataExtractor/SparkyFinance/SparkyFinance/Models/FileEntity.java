package com.DataExtractor.SparkyFinance.SparkyFinance.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;

@Document(collection="files")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Id
    private String id;

    private String fileName;
    private String contentType;
    private long size;
    private byte[] data;

}