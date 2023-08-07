package com.DataExtractor.SparkyFinance.SparkyFinance.Services;


import org.apache.spark.sql.SparkSession;

//This Class is used for Initialize  SparkWorking Server
public class SparkyInitializer {


    //Start SparkServer
    public SparkSession startSparky(String Appname, String master) {
        // Create a SparkSession using the SparkConf
        SparkSession spark = SparkSession.builder()
                .appName(Appname)
                .master(master)
                .getOrCreate();

        System.out.println("Spark Session Starts >>>> ");
        // Your Spark application code goes here...

        // Stop the SparkSession when done
        return spark;
    }


}
