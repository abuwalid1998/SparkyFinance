package com.DataExtractor.SparkyFinance.SparkyFinance.Controller;

import com.mongodb.client.gridfs.GridFSBucket;

import com.DataExtractor.SparkyFinance.SparkyFinance.FileManger.FileTools;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.FileEntity;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.ResponseMessage;
import com.DataExtractor.SparkyFinance.SparkyFinance.Models.SparkyConfig;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.FileServiceMongo;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkDataManeger;
import com.DataExtractor.SparkyFinance.SparkyFinance.Services.SparkyInitializer;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.spark.sql.SparkSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/Sparky")
public class MainController {


    private final GridFSBucket gridFSBucket;

    SparkyInitializer sparkystarter;



    SparkSession sparkSession;


    FileTools fileTools;



    private final FileServiceMongo fileServiceMongo;

    public MainController(FileServiceMongo fileServiceMongo, GridFSBucket gridFSBucket) {
        this.fileServiceMongo = fileServiceMongo;
        this.gridFSBucket = gridFSBucket;
    }


    @GetMapping("/StartSparkyServer")
    public ResponseEntity<String> StartSpark(@RequestBody SparkyConfig sparkyConfig) {

        try {


            sparkystarter = new SparkyInitializer();


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

            String result  = maneger.CleanData(filepath,sparkSession);

            fileServiceMongo.saveFile(file);

            return ResponseEntity.ok(result);

        }catch (Exception e){

            return ResponseEntity.ok(e.getMessage());

        }

    }

    @CrossOrigin(origins = "http://localhost:9110")
    @GetMapping("/check-file/{filename}")
    public ResponseEntity<Map<String, Boolean>> checkFileAvailability(@PathVariable String filename) {

        System.out.println("*************************");
        boolean result =(fileServiceMongo.getFileByName(filename));
        System.out.println("*************************");

        Map<String, Boolean> response = Collections.singletonMap("exists", result);
        return ResponseEntity.ok(response);

    }

    @CrossOrigin(origins = "http://localhost:9110")
    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) {

        FileEntity fileEntity = fileServiceMongo.getFileEntitiesByFileName(filename);

        if (fileEntity != null) {

            String gridFsFileId = fileEntity.getFileName(); // Assuming you store the GridFS file's ObjectId

            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFsFileId);
            GridFsResource resource = new GridFsResource(downloadStream.getGridFSFile());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }

        // Handle case when the file is not found
        return ResponseEntity.notFound().build();
    }
}





