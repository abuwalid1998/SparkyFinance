package Controller;


import FileManger.FileTools;
import Models.InFileDecription;
import Services.SparkyInitializer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileDescriptor;

@RestController
@RequestMapping("/Sparky")
public class MainController {

    SparkyInitializer sparkystarter;
    FileTools fileTools;

    @GetMapping("/StartSparkyServer")
    public ResponseEntity<String> StartSpark() {

        try {

            return ResponseEntity.ok("Sparky Started Successfully");

        }catch (Exception e)
        {

            return  ResponseEntity.ok(e.getMessage());

        }


    }
    @PostMapping(value = "/SaveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InFileDecription> GetFile(@RequestParam("file") MultipartFile infile)
    {

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
