package com.DataExtractor.SparkyFinance.SparkyFinance.Models;


import lombok.*;

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
