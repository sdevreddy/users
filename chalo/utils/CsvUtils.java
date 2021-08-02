package com.chalo.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CsvUtils
{
    public static List<List<String>> getListFromCsv(String file) throws IOException
    {
        String line = "\n";
        String splitBy = ",";
        List<List<String>> data=new ArrayList<>();
        try
        {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] employee = line.split(splitBy);    // use comma as separator
                List<String> lineData = Arrays.asList( employee );
                data.add(lineData);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return  data;
    }

    public static void writeDataLineByLine(String filePath,String[] data1) {
        File file = new File(filePath);
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeNext(data1);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataAtOnce(String filePath, List<String[]> data) {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);        try {
            System.out.println(data);
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);            // create a List which contains String array            writer.writeAll(data);            // closing writer connection
            writer.writeAll(data);
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

