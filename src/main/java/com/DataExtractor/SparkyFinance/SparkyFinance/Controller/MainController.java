package com.DataExtractor.SparkyFinance.SparkyFinance.Controller;

import com.DataExtractor.SparkyFinance.SparkyFinance.FileManger.FileTools;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.ResponseMessage;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.SparkyConfig;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.FileServiceMongo;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkDataManeger;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkyInitializer;
import lombok.var;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/Sparky")
public class MainController {


    SparkyInitializer sparkystarter;


    SparkSession sparkSession;

    final
    FileServiceMongo fileServiceMongo;

    public MainController(FileServiceMongo fileServiceMongo) {
        this.fileServiceMongo = fileServiceMongo;
    }


    @CrossOrigin(origins = "http://localhost:9110")
    @PostMapping("/StartSparkyServer")
    public ResponseEntity<ResponseMessage> StartSpark(@RequestBody SparkyConfig sparkyConfig) {

        try {


            sparkystarter = new SparkyInitializer();


            System.out.println(sparkyConfig.getAppname());
            System.out.println(sparkyConfig.getMaster());

            sparkSession = sparkystarter.startSparky(sparkyConfig.getAppname(),sparkyConfig.getMaster());

            return ResponseEntity.ok(new ResponseMessage(sparkSession.sessionUUID()));

        }catch (Exception e)
        {


            System.out.println("Im Here");
            System.out.println(e.getMessage());
            return  ResponseEntity.ok(new ResponseMessage(e.getMessage()));

        }


    }


    @Value("${upload.path}") // Specify this in your application.properties
    private String uploadPath;


    @CrossOrigin(origins = "http://localhost:9110")
    @PostMapping("/ScanFile/{filename}")
    public ResponseEntity<Map<String, Boolean>> scanFile(@PathVariable String filename) {
        try {
            String filepath = uploadPath + File.separator + filename;

            SparkDataManeger maneger = new SparkDataManeger();

            System.out.println("Readed File :- " + filepath);

            maneger.CleanData(filepath, sparkSession);

            boolean flag = fileServiceMongo.uploadFile(filepath); // Pass the filename instead of MultipartFile

            Map<String, Boolean> response = Collections.singletonMap("isdone", flag);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Boolean> response = Collections.singletonMap("isdone", false);
            return ResponseEntity.ok(response);
        }
    }


}





