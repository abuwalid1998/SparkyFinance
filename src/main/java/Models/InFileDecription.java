package Models;


import lombok.*;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InFileDecription {

    private String filepath;
    private String filesize;
    private String filext;
    private String message;

}
