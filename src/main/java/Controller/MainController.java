package Controller;


import FileManger.FileTools;
import Models.InFileDecription;
import Models.SparkyConfig;
import Services.SparkDataManeger;
import Services.SparkyInitializer;
import org.apache.spark.sql.SparkSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/Sparky")
public class MainController {

    final SparkyInitializer sparkystarter = new SparkyInitializer();
    SparkSession sparkSession;
    FileTools fileTools;

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
    public ResponseEntity<InFileDecription> GetFile(@RequestParam("file") MultipartFile infile) {

        try {

            fileTools = new FileTools();
            String FileSize = "File Size : " + infile.getSize();


            InFileDecription fileDecription = new InFileDecription(
                    infile.getName(),
                    FileSize,
                    fileTools.getFileExtension(infile.getOriginalFilename()),
                    "File Imported Successfully"
            );

            return ResponseEntity.ok(fileDecription);

        }catch (Exception e){

           return ResponseEntity.ok(new InFileDecription("Not Found","0 MB","NULL",e.getMessage()));

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
