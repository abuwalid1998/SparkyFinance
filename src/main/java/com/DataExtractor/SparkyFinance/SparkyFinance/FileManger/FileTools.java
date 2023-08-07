package com.DataExtractor.SparkyFinance.SparkyFinance.FileManger;


import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
public class FileTools {




    public  String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex >= 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            // File name has no extension
            return "";
        }
    }


    public String GetFilePath(MultipartFile file) throws IOException {

        String uploadDir = "src/main/resources/Files/";

        // Create the directory if it doesn't exist
        Files.createDirectories(Paths.get(uploadDir));

        // Generate a unique file name for the uploaded file
        String fileName = file.getOriginalFilename();

        // Save the file to the server
        Path filePath = Paths.get(uploadDir + fileName);

        Files.write(filePath, file.getBytes());


        return filePath.toString();
    }

}
