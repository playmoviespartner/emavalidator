/* Copyright 2014 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package emavalidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;

/**
 * The main entry point for the EMA Validator project where users can execute the validator directly from the command line.
 * Uses a set of command line parameters that should be sent in during execution time. See RunTimeParameters documentation or
 * the file included with this project titled 'README'
 * @author canavan
 */
public class Main
{
    /**
     * Accept input parameters for running the validator from the command line.
     * These pertain to the format of the error log and how to save the error log itself.
     * @param args The list of input parameters in strong format received from the calling console. Please see RunTimeParameters for all the available parameters.
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static void main(String[] args) throws SAXException, ParserConfigurationException
    {
        RuntimeParameters rtp = new RuntimeParameters();
        new JCommander(rtp, args);
        ErrorLog.OUTPUT_LOG_TYPE outputLogType = ErrorLog.OUTPUT_LOG_TYPE.LOG;
        int resultSum = 0;
        ValidatorResults validatorResults = null;

        if(rtp.isCsvFormattedErrorLog() && rtp.isLogFormattedErrorLog())
            System.out.println("Detected both CSV and Log output requested. Defauling to Log. Please double check your parameters in the future");
        else if(rtp.isCsvFormattedErrorLog())
        {
            System.out.println("Detected CSV output. Formatting error results in CSV format.");
            outputLogType = ErrorLog.OUTPUT_LOG_TYPE.CSV;
        }
        else
            System.out.println("Detected Log output. Formatting error results in Log format.");

        if(rtp.getInputFilePath().endsWith(".txt") || rtp.getInputFilePath().endsWith(".csv")) // if the input file is text formatted. either CSV or txt file is fine since both would have the same encoding scheme
        {
            try
            {
                BufferedReader bufferedCSVReader = new BufferedReader(new FileReader(rtp.getInputFilePath()));
                System.out.println("CSV file successfully opened and preparing for validation.");
                validatorResults = EMAResourceValidator.validateEMACSVFile(rtp.getInputFilePath(), bufferedCSVReader, outputLogType.toString());
                System.out.println("CSV file successfully validated and output contents retrieved.");
            }
            catch (FileNotFoundException FNFE) // catch text related exceptions here only. throw them to the surrounding catch clause with context.
            {
                System.out.println("File could not be located on any local or remote storage disks. Please correct your input parameters and run again.");
                FNFE.printStackTrace();
                System.exit(1);
            }
            catch (IOException IOE)
            {
                System.out.println("There was an IOException that occured during parsing of the input CSV sheet by the Apache Commons CSV parser.");
                System.out.println("Please verify the integrity of the input CSV file and run again.");
                IOE.printStackTrace();
                System.exit(1);
            }
        }
        else if(rtp.getInputFilePath().endsWith(".xls") || rtp.getInputFilePath().endsWith("xlsx")) // if the input file is a workbook. either XLS or XLSX is fine
        {
            try
            {
                InputStream is = new FileInputStream(new File(rtp.getInputFilePath()));
                System.out.println("Excel WorkBook file successfully opened and preparing for validation.");
                validatorResults = EMAResourceValidator.validateEMAXLSXAsCSV(is, outputLogType.toString());
                                
//                formattedOutput = EMAResourceValidator.validateEMAWorkbook(WorkbookFactory.create(new File(rtp.getInputFilePath())), outputLogType.toString()); // original
                System.out.println("Excel file successfully validated and output contents retrieved.");
            }
            catch (org.apache.poi.POIXMLException POIXMLE) // catch Apache POI or spreadsheet-based exceptions here only. throw them to the surrounding catch clause with context.
            {
                System.out.println("The input workbook could not be validated because it's corrupted. Please verify its integrity and try again.");
                POIXMLE.printStackTrace();
                System.exit(1);
            }
            catch (IOException IOE)
            {
                System.out.println("There was an IOException that occured during parsing of the input CSV sheet by the Apache Commons CSV parser.");
                System.out.println("Please verify the integrity of the input CSV file and run again.");
                IOE.printStackTrace();
                System.exit(1);
            }
            catch (SAXException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ParserConfigurationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if(rtp.getInputFilePath().endsWith(".xml"))
        {
            try
            {
            // Process XML format
            String fileName = rtp.getInputFilePath();
            InputStream is = new FileInputStream(new File(fileName));
            System.out.println("XML file successfully opened and preparing for validation");
            validatorResults = EMAResourceValidator.validateEMAXML(fileName, is, outputLogType.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }

        if(rtp.isLoggingToConsole())
            resultSum += Main.printFormattedOutputToConsole(validatorResults.getResults());

        if(rtp.isLoggingToFile())
            resultSum += Main.printFormattedOutputToFile(validatorResults.getResults(), rtp.getOutputFilePath());

        if(resultSum == 0)
            System.out.println("Execution completed successfully. Exiting.");
        else
            System.out.println("Execution did not complete successfully. Exiting.");
        System.exit(resultSum);
    }

    /**
     * @param formattedOutput The formatted error output received from the ErrorLog to print to a file
     * @param fileName The name of the file where the results should be logged to. Relative or fully qualified path as desired.
     */
    private static int printFormattedOutputToFile(String formattedOutput, String fileName)
    {
        if(fileName == null || fileName.isEmpty())
        {
            String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss", Locale.ENGLISH).format(new Date());
            fileName = "Validator Results " + currentTimeStamp + ".txt";
        }
        File outputFile = new File(fileName);
        FileWriter fileWriter;

        try
        {
            fileWriter = new FileWriter(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(formattedOutput);
            bufferedWriter.close();
            fileWriter.close();
        }
        catch (IOException IOE)
        {
            System.out.println("if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason");
            IOE.printStackTrace();
            return 1;
        }

        System.out.println("Wrote output contents to file: " + fileName);
        System.out.println("If the file already existed, it was overwritten. If it didn't already exist, it was created.");

        return 0;
    }

    /**
     * @param formattedOutput The formatted error output received from the ErrorLog to print to the standard console
     */
    private static int printFormattedOutputToConsole(String formattedOutput)
    {
        System.out.println(formattedOutput);
        System.out.println("Wrote output contents to console.");
        return 0;
    }
}
