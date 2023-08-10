package com.DataExtractor.SparkyFinance.SparkyFinance.Controller;

import com.DataExtractor.SparkyFinance.SparkyFinance.Models.ResponseMessage;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.FileServiceMongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/Sparky/files")
public class FileController {

    @Value("${upload.path}") // Specify this in your application.properties
    private String uploadPath;

    private final FileServiceMongo fileServiceMongo;

    public FileController(FileServiceMongo fileServiceMongo) {
        this.fileServiceMongo = fileServiceMongo;
    }


    @CrossOrigin(origins = "http://localhost:9110")
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("File is empty"));
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);

            return ResponseEntity.ok(new ResponseMessage("File uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Error unable to upload file"));
        }
    }

    @CrossOrigin(origins = "http://localhost:9110")
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath + File.separator + filename);

            File file = filePath.toFile();

            InputStream inputStream = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            inputStream.read(fileContent);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }




}

    @CrossOrigin(origins = "http://localhost:9110")
    @GetMapping("/check-file/{filename}")
    public ResponseEntity<Map<String, Boolean>> checkFileAvailability(@PathVariable String filename) {

        boolean result;
        Path filePath = Paths.get(uploadPath, filename);

        if (Files.exists(filePath)) {
            result = true;
            System.out.println("File exists!");
        } else {
            result=false;
            System.out.println("File does not exist.");
        }

        Map<String, Boolean> response = Collections.singletonMap("exists", result);
        return ResponseEntity.ok(response);

    }
}
