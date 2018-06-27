import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.apache.log4j.BasicConfigurator;

public class DataReaderAndPoster {

   /* class where its instance holds data for one sheet */
   static class Data {
      public static final Integer NUMBER_OF_DATA_ENTRIES = 4;
      double[] numberSetOne = new double[NUMBER_OF_DATA_ENTRIES];
      double[] numberSetTwo = new double[NUMBER_OF_DATA_ENTRIES];
      String[] wordSetOne = new String[NUMBER_OF_DATA_ENTRIES];
   }


   private static final String FILE_NAME_1 = "./Data1.xlsx";
   private static final String FILE_NAME_2 = "./Data2.xlsx";

   public static void main(String[] args) throws Exception {

      /* Pull the sheets filled with data from the xlsx files */
      FileInputStream excelFile_1 = new FileInputStream(new File(FILE_NAME_1));
      FileInputStream excelFile_2 = new FileInputStream(new File(FILE_NAME_2));
      Workbook workbook_1 = new XSSFWorkbook(excelFile_1);
      Workbook workbook_2 = new XSSFWorkbook(excelFile_2);
      Sheet dataSheet_1 = workbook_1.getSheetAt(0);
      Sheet dataSheet_2 = workbook_2.getSheetAt(0);

      /* Create arrays to hold the data from the sheets using a custom class data,
         as well as initializing the final data arrays */
      Data data_1 = new Data();
      Data data_2 = new Data();
      Data finalData = new Data();

      int columnCount = 0; // keeps track of which column we are at in the datasheets

      /* Extract data from Data1.xlsx and put into our data arrays */
      for (Row row: dataSheet_1) {
         // if this is the first row, skip
         if(row.getRowNum() == 0) {
            continue;
         }
         columnCount = 0; // reset column count
         for(Cell cell: row) {
            // for each column, add data to the corresponding arraylist
            switch(columnCount) {
               case 0:
                  data_1.numberSetOne[row.getRowNum() - 1] = cell.getNumericCellValue();
                  break;
               case 1:
                  data_1.numberSetTwo[row.getRowNum() - 1] = cell.getNumericCellValue();
                  break;
               case 2:
                  data_1.wordSetOne[row.getRowNum() - 1] = cell.getStringCellValue();
                  break;
            }
            columnCount++;
         }
      }
      /* Extract data from Data2.xlsx and put into our data arrays */
      for (Row row: dataSheet_2) {
         // if this is the first row, skip
         if(row.getRowNum() == 0) {
            continue;
         }
         columnCount = 0; // reset column count
         for(Cell cell: row) {
            // for each column, add data to the corresponding arraylist
            switch(columnCount) {
               case 0:
                  data_2.numberSetOne[row.getRowNum() - 1] = cell.getNumericCellValue();
                  break;
               case 1:
                  data_2.numberSetTwo[row.getRowNum() - 1] = cell.getNumericCellValue();
                  break;
               case 2:
                  data_2.wordSetOne[row.getRowNum() - 1] = cell.getStringCellValue();
                  break;
            }
            columnCount++;
         }
      }

      /* Perform our ops on the data */

      /* Multiply data_1's numberSetOne data with data_2's numberSetOne data,
       * divide data_1's numberSetTwo data by data_2's numberSetTwo data,
       * and concatenate data_1's wordSetOne data with data_2's wordSetOne data */
      for(int i = 0; i < data.NUMBER_OF_DATA_ENTRIES; i++) {
         finalData.numberSetOne[i] = data_1.numberSetOne[i] * data_2.numberSetOne[i];
         finalData.numberSetTwo[i] = data_1.numberSetTwo[i] / data_2.numberSetTwo[i];
         finalData.wordSetOne[i] = data_1.wordSetOne[i] + " " + data_2.wordSetOne[i];
      }

      // Configures log4j, a logger
      BasicConfigurator.configure();

      /* Create our JSONObject and fill with the 4 attribute value pairs */
      JSONObject obj = new JSONObject();
      obj.put("id", "damonngo96@gmail.com");

      // create JSONArray for the three data arrays and fill them up
      JSONArray numberSetOneList = new JSONArray();
      JSONArray numberSetTwoList = new JSONArray();
      JSONArray wordSetOneList = new JSONArray();

      for(int i = 0; i < data.NUMBER_OF_DATA_ENTRIES; i++) {
         numberSetOneList.add(finalData.numberSetOne[i]);
         numberSetTwoList.add(finalData.numberSetTwo[i]);
         wordSetOneList.add(finalData.wordSetOne[i]);
      }

      obj.put("numberSetOne", numberSetOneList);
      obj.put("numberSetTwo", numberSetTwoList);
      obj.put("wordSetOne", wordSetOneList);

      /* Finally, post our request to the server */
      String postUrl = "http://34.239.125.159:5000/challenge";
      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpPost post = new HttpPost(postUrl);
      StringEntity postingString = new StringEntity(obj.toString());

      post.setEntity(postingString);
      post.setHeader("Content-type", "application/json");
      HttpResponse response = httpClient.execute(post);
   }
}

