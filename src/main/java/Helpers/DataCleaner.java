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

            System.setProperty("org.codehaus.janino.debug", "true");

            System.out.println("Data cleaning completed and saved to " + filePath);
            // Write the cleaned data to the same Excel file
            writeDataFrameToExcel(df,"Files/output.xlsx");

            System.out.println("Data cleaning completed and saved to " + filePath);
            spark.stop();

            return true;

        }catch (Exception e){

            System.out.println( "Error Happen :- " );
            e.printStackTrace();
            return false;

        }
    }


    private  void writeDataFrameToExcel(Dataset<Row> df, String outputPath) {

        System.setProperty("org.codehaus.janino.debug", "true");
        // Convert DataFrame to an array of Rows
        Row[] rows = (Row[]) df.collect();

        // Create a Workbook
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

        // Create a Sheet
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Sheet1");

        // Create a header row
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        String[] columns = df.columns();
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // Create data rows
        for (int i = 0; i < rows.length; i++) {
            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < columns.length; j++) {
                dataRow.createCell(j).setCellValue(rows[i].get(j).toString());
            }
        }

        try {
            // Write the Workbook to a file
            java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outputPath);
            workbook.write(fileOut);
            fileOut.close();

            System.out.println("DataFrame written to Excel file: " + outputPath);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }







}
