package Helpers;

import Services.SparkyInitializer;
import lombok.NoArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@NoArgsConstructor
public class DataCleaner {


    public boolean cleanExcelData(String filePath,SparkSession spark) {

        try {
            Dataset<Row> df = spark.read()
                    .format("com.crealytics.spark.excel")
                    .option("header", "true") // Assuming the first row contains column names
                    .load(filePath);

            // Filter out rows with null values
            df = df.na().drop();

            // Write the cleaned data to the same Excel file
            df.write()
                    .format("com.crealytics.spark.excel")
                    .option("header", "true")
                    .mode("overwrite") // This will overwrite the existing file with the cleaned data
                    .save("src/main/resources/Files/RESULT.XLSX");

            System.out.println("Data cleaning completed and saved to " + filePath);
            spark.stop();

            return true;

        }catch (Exception e){

            System.out.println( "Error Happen :- "+ e.getMessage() );
            return false;

        }
    }







}
