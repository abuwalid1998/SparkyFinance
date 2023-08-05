package Services;

import Helpers.DataCleaner;
import org.apache.spark.sql.SparkSession;

public class SparkDataManeger {


    final DataCleaner cleaner = new DataCleaner();

    public String CleanData(String file , SparkSession sparkSession){

        try {

          boolean flag =  cleaner.cleanExcelData(file,sparkSession);
          if (flag){
            return "File Cleaned:- " + file;
          }else{
              return "File Not Cleaned:- " + file;
          }

        }catch(Exception e){
            return e.getMessage();
        }

    }




}
