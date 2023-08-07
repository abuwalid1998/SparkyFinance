package com.DataExtractor.SparkyFinance.SparkyFinance.Controller;


import com.DataExtractor.SparkyFinance.SparkyFinance.FileManger.FileTools;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.ResponseMessage;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.SparkyConfig;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.FileServiceMongo;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkDataManeger;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkyInitializer;
import org.apache.spark.sql.SparkSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/Sparky")
public class MainController {



    SparkyInitializer sparkystarter;


    SparkSession sparkSession;


    FileTools fileTools;



    private final FileServiceMongo fileServiceMongo;

    public MainController(FileServiceMongo fileServiceMongo) {
        this.fileServiceMongo = fileServiceMongo;
    }


    @GetMapping("/StartSparkyServer")
    public ResponseEntity<String> StartSpark(@RequestBody SparkyConfig sparkyConfig) {

        try {



            System.out.println(sparkyConfig.getAppname());
            System.out.println(sparkyConfig.getMaster());

            sparkSession = sparkystarter.startSparky(sparkyConfig.getAppname(),sparkyConfig.getMaster());

            return ResponseEntity.ok(sparkSession.sessionUUID());

        }catch (Exception e)
        {


            System.out.println("Im Here");
            System.out.println(e.getMessage());
            return  ResponseEntity.ok(e.getMessage());

        }


    }

    @PostMapping(value = "/SaveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "http://localhost:9110")
    public ResponseEntity<ResponseMessage> GetFile(@RequestParam("file") MultipartFile infile) {

        try {
            fileServiceMongo.saveFile(infile);
            return ResponseEntity.ok(new ResponseMessage("File Saved Successfully"));

        }catch (Exception e){


            System.out.println(e.getMessage());
           return ResponseEntity.ok(new ResponseMessage("File Not Saved :- Error Happen"));

        }

    }


    @PostMapping(value = "/ScanFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> Scanfile(@RequestParam("file") MultipartFile file) throws IOException {

        try {
            String filepath = new FileTools().GetFilePath(file);

            SparkDataManeger maneger = new SparkDataManeger();

            System.out.println("Readed File :- " + filepath);

            return ResponseEntity.ok(maneger.CleanData(filepath,sparkSession));

        }catch (Exception e){

            return ResponseEntity.ok(e.getMessage());

        }

    }

}
