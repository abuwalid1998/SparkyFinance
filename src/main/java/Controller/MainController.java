package Controller;


import Services.SparkyInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Sparky")
public class MainController {

    SparkyInitializer sparkystarter;

    @GetMapping("/StartSparkyServer")
    public ResponseEntity<String> StartSpark()
    {

        try {


            return ResponseEntity.ok("Sparky Started Successfully");

        }catch (Exception e)
        {

            return  ResponseEntity.ok(e.getMessage());

        }


    }


}
