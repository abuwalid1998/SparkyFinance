package Controller;


import FileManger.FileTools;
import Models.InFileDecription;
import Models.SparkyConfig;
import Services.SparkyInitializer;
import org.apache.spark.sql.SparkSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/Sparky")
public class MainController {

    SparkyInitializer sparkystarter;
    FileTools fileTools;

    @GetMapping("/StartSparkyServer")
    public ResponseEntity<String> StartSpark(@RequestBody SparkyConfig sparkyConfig) {

        try {

            sparkystarter = new SparkyInitializer();

            System.out.println(sparkyConfig.getAppname());
            System.out.println(sparkyConfig.getMaster());

            SparkSession conf = sparkystarter.startSparky(sparkyConfig.getAppname(),sparkyConfig.getMaster());

            return ResponseEntity.ok(conf.sessionUUID());

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

}
