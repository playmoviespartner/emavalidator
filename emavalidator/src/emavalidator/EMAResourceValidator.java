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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.SAXException;

import emavalidator.AbstractEMASpec.EMAVersion;
import emavalidator.utils.EMAVersionParser;
import emavalidator.utils.XLSX2CSV;

/**
 * The wrapper class created to validate multiple sheets in the same workbook. Stores all the contextual information on
 * where the data is located for each sheet, prepares the error log for a new sheet's worth of errors, and makes sure all
 * the columns in the verifying spec match the order of the columns input by the user.
 * @author canavan
 */
public final class EMAResourceValidator
{
    /**
     * For use in storing the entire row's contents in a single string to determine its validity. Used as the separating character between each value.
     */
    public static final String SPLIT_CHAR = "`";

    /**
     * The number of columns that a user could conceivably add to the EMA spec before ruining it and making it impossible to tell which version is which by column count alone.
     */
    private static final int COLUMN_HEADER_COUNT_MAX_DIFF = 3;

    /**
     * The column section headers row index location, 0th based, not always present. Should come after the first non blank row, if the section headers exist
     */
    private static int sectionHeaderRowIndex = -1;

    /**
     * The column headers row index location, 0th based, should always be present. Should come after sectionHeaderRowIndex, if there is one, else it should be after the first non blank row
     */
    private static int columnHeaderRowIndex  = -1;

    /**
     * The last row that a comment row appears on, 0th based, not always present. Should come after columnHeaderRowIndex
     */
    private static int lastCommentRowIndex   = -1;

    /**
     * The first row that actual data appears on, 0th based, not always present. Should come after lastCommentRowIndex, if there is one, else after columnHeaderRowIndex
     */
    private static int dataStartsRowIndex    = -1;

    /**
     * The first row that is not blank in the input sheet. Indexes that follow after, in order: sectionHeaderRowIndex, columnHeaderRowIndex,lastCommentRowIndex, dataStartsRowIndex
     */
    private static int firstNonEmptyRow = -1;
    
    /**
     * The number of input rows from the input source to search for column header sections, column header definitions, and possible comment rows.
     */
    private static final int NUM_ROWS_FOR_HEADER_SEARCH = 10;
    
    /**
     * Private constructor to promote the fact that all methods are static.
     */
    private EMAResourceValidator() {}


    /**
     * Validate the incoming CSV file by automatically detecting the EMA version, location of the input data, number of column header rows, and location of each EMA column definition
     * @param fileName The name of the file that is to be validated. This is not used to open the file directly
     * @param inputFileReader A buffered reader that's wrapped around the input file to be validated
     * @param logOutputType The formatting of the output log that's requested. Please see ErrorLog.OUTPUT_LOG_TYPE for output types
     * @return A pretty string representing all the errors that occurred during validation. Formatted as requested by the logOutputType input parameter and pulled from the ErrorLog class.
     * @throws IOException If there are any issues handling the file input / reading
     */
    public static ValidatorResults validateEMACSVFile(String fileName, BufferedReader inputFileReader, String logOutputType) throws IOException
    {
//        Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.parse(inputFileReader);
        ValidatorResults validatorResults;
        CSVParser parser = new CSVParser(inputFileReader, CSVFormat.EXCEL);
        List<CSVRecord> csvRecords = parser.getRecords();
        parser.close();
        ErrorLog.setErrorLogType(logOutputType);
        ErrorLog.clearErrorLog();
        ArrayList<String> columnHeaderRows = EMAResourceValidator.getColumnHeaderRows(csvRecords);
        EMAResourceValidator.resetHeaderRowIndexValues(); // get rid of all the old values. stupid static classes :(
        // find out what row the actual data starts on in the sheet,   EMAWorkbookValidator.dataStartsRowIndex
        // find out what row the main header values are located on,    EMAWorkbookValidator.columnHeaderRowIndex
        // find out what row the section header values are located on, EMAWorkbookValidator.sectionHeaderRowIndex
        // find out what row the last comment row is,                  EMAWorkbookValidator.lastCommentRowIndex
        // find out what row is the first non empty row,               EMAWorkbookValidator.firstNonEmptyRow
        EMAResourceValidator.findHeaderRowIndexes(columnHeaderRows, fileName);
        ArrayList<String> columnHeaderDefinitions = EMAResourceValidator.getColumnHeaderValues(columnHeaderRows.get(EMAResourceValidator.columnHeaderRowIndex));
        // decide the EMA version from the list of user defined columns from the input sheet
        AbstractEMASpec.EMAVersion emaVersion = EMAResourceValidator.getEMAVersionNumber(columnHeaderDefinitions);
        // instantiate the corresponding EMA spec based on the EMA version that was dynamically decided
        AbstractEMASpec emaSpec = AbstractEMASpec.getInstance(emaVersion);
        // send the current sheet's properties to the error log so that it can correctly format and analyze incoming errors
        ErrorLog.setCurrentSheet(fileName, emaVersion, emaSpec, 0);
        // reorder the columns in the EMA spec based off of the user's input which doesn't necessarily match the spec itself
        emaSpec.getColumnSpec().reorderColumnDefinitions(columnHeaderDefinitions, EMAResourceValidator.columnHeaderRowIndex);
        // Verify that all column headers are present for an emaVersion.
        emaSpec.getColumnSpec().verifyColumnDefinitions(emaVersion, columnHeaderDefinitions);
        // validate the sheet with the custom user column input spec
        emaSpec.validate(csvRecords, EMAResourceValidator.dataStartsRowIndex);
        validatorResults = new ValidatorResults(ErrorLog.getFormattedErrorLog(), "csv", false);
        return validatorResults;
    }
    
    public static ValidatorResults validateEMAXLSXAsCSV(InputStream inputStream, String logOutputType) throws IOException, SAXException, ParserConfigurationException 
    {
        ValidatorResults validatorResults = null;
        OPCPackage pkg;
        try
        {
            pkg = OPCPackage.open(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            XLSX2CSV xlsx2csv = new XLSX2CSV(pkg, ps, -1);
            
            System.out.println("Doing internal conversion of .xlsx into .csv.");
            String sheetName = xlsx2csv.process();
            System.out.println(".xlsx conversion to .csv complete.");
            
            BufferedReader bufferedCSVReader = new BufferedReader(new StringReader(baos.toString()));
            
            validatorResults = EMAResourceValidator.validateEMACSVFile(sheetName, bufferedCSVReader, logOutputType);
        }
        catch (InvalidFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (OpenXML4JException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return validatorResults;
    }

    /**
     * Validate the incoming workbook by automatically detecting the EMA version, location of the input data, number of column header rows, and location of each EMA column definition
     * @param inputWorkbook The org.apache.poi.ss.usermodel.Workbook to that needs to be validated. Will validate all valid worksheets that are inside the workbook.
     * @param logOutputType The formatting of the output log that's requested. Please see ErrorLog.OUTPUT_LOG_TYPE for output types
     * @return A pretty string representing all the errors that occurred during validation. Formatted as requested by the logOutputType input parameter and pulled from the ErrorLog class.
     */
    public static ValidatorResults validateEMAWorkbook(Workbook inputWorkbook, String logOutputType)
    {
        ValidatorResults validatorResults;
        int sheetCount = inputWorkbook.getNumberOfSheets(); // find the number of sheets to validate
        ErrorLog.setErrorLogType(logOutputType);
        ErrorLog.clearErrorLog(); // Clear out the error log before we start any work.
        for(int x = 0; x < sheetCount; x++) // go through every sheet
        {
            Sheet currentSheet = inputWorkbook.getSheetAt(x);  // get the current sheet to validate
            if(!isValidSheetName(currentSheet.getSheetName())) // check the name of the sheet
                continue;                                      // do not process sheets with invalid names

            // get the first 10 rows from the sheet which should hopefully contain all the header rows that we need
            ArrayList<String> columnHeaderRows = EMAResourceValidator.getColumnHeaderRows(currentSheet);
            EMAResourceValidator.resetHeaderRowIndexValues(); // get rid of all the old values. stupid static classes :(
            // find out what row the actual data starts on in the sheet,   EMAWorkbookValidator.dataStartsRowIndex
            // find out what row the main header values are located on,    EMAWorkbookValidator.columnHeaderRowIndex
            // find out what row the section header values are located on, EMAWorkbookValidator.sectionHeaderRowIndex
            // find out what row the last comment row is,                  EMAWorkbookValidator.lastCommentRowIndex
            // find out what row is the first non empty row,               EMAWorkbookValidator.firstNonEmptyRow
            EMAResourceValidator.findHeaderRowIndexes(columnHeaderRows, currentSheet.getSheetName());
            ArrayList<String> columnHeaderDefinitions = EMAResourceValidator.getColumnHeaderValues(columnHeaderRows.get(EMAResourceValidator.columnHeaderRowIndex));
            // decide the EMA version from the list of user defined columns from the input sheet
            AbstractEMASpec.EMAVersion emaVersion = EMAResourceValidator.getEMAVersionNumber(columnHeaderDefinitions);
            // instantiate the corresponding EMA spec based on the EMA version that was dynamically decided
            AbstractEMASpec emaSpec = AbstractEMASpec.getInstance(emaVersion);
            // send the current sheet's properties to the error log so that it can correctly format and analyze incoming errors
            ErrorLog.setCurrentSheet(currentSheet.getSheetName(), emaVersion, emaSpec, x);
            // reorder the columns in the EMA spec based off of the user's input
            emaSpec.getColumnSpec().reorderColumnDefinitions(columnHeaderDefinitions, EMAResourceValidator.columnHeaderRowIndex);
            // validate the sheet with the custom user column input spec
            emaSpec.validate(currentSheet, EMAResourceValidator.dataStartsRowIndex);
        }
        validatorResults = new ValidatorResults(ErrorLog.getFormattedErrorLog(), "xls", false);
        return validatorResults;
    }
    
    /**
     * Validate the incoming XML file against an avails XSD Template.
     * XSD Template version is automatically chosen by parsing the XML for
     * the version URL under the namespace 'xmlns' attribute in AvailList tag.
     * @param fileName The string name of the xml file to validate.
     * @param inputStream The stream of the fileName file opened for reading
     * @param logOutputType The formatting of the output log that's requested. Please see ErrorLog.OUTPUT_LOG_TYPE for output types
     * @return String printing out whether the XML validates against the XSD or not.
     */
    public static ValidatorResults validateEMAXML(String fileName, InputStream inputStream, String logOutputType) throws SAXException, ParserConfigurationException 
    {
        String xsdFilePath;
        String xsdTemplateVersion;
        String xmlAvailsVersion;

        ErrorLog.setErrorLogType(logOutputType);
        ErrorLog.clearErrorLog();
        
        // Copy inputStream to be used in multiple places.
        ByteArrayOutputStream baos = copyInputStream(inputStream);
        
        // Open new InputStreams using the recorded bytes
        // Can be repeated as many times as you wish
        InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray()); 
        
        EMAVersionParser emaVersionParser = new EMAVersionParser(fileName, is1);
        
        // Parse the XML for the avails version number.
        xmlAvailsVersion = emaVersionParser.process();
                
        // Dynamically determine the avails-version to use based on the xmlns namespace in the validating XML.
        if ( xmlAvailsVersion.contains("v1.6") ) 
        {
            xsdFilePath = "avails-v1.6a.xsd";
            xsdTemplateVersion = "avails-v1.6a";
        } 
        else if ( xmlAvailsVersion.contains("v2.0") )
        {
            xsdFilePath = "avails-v2.0.xsd";
            xsdTemplateVersion = "avails-v2.0";
        } 
        else if ( xmlAvailsVersion.contains("v2.1") )
        {
            xsdFilePath = "avails-v2.1.xsd";
            xsdTemplateVersion = "avails-v2.1";
        }
        else 
        {
            xsdFilePath = "avails-v1.6a.xsd";
            xsdTemplateVersion = "avails-v1.6a";
        }
        
        // Validate XML with XSD Template first.
        XSDValidator xsdValidator = new XSDValidator();
        boolean isXMLValidated = xsdValidator.validateXMLSchema(xsdFilePath, is2);
        ValidatorResults validatorResults = new ValidatorResults();
        String msg = "";
        if( isXMLValidated )
        {
            msg = String.format("%s validated successfully against Schema %s", fileName, xsdTemplateVersion);

            // Create factory parser
            try 
            {
                //EMAXMLParser xmlParser = new EMAXMLParser(fileName);
                //xmlParser.process(); // hold parsed result in xmlParser object, use the results in validate function?
                
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }            
        } 
        else
        {
            msg = String.format("XML is invalid: %s", xsdValidator.getErrorMessage());
        }
        // Parse XML into data structure.
        
        // Validate XML
        validatorResults.setResults(msg);
        validatorResults.setFileType("xml");
        validatorResults.setIsXmlValid(isXMLValidated);
        return validatorResults;
    }

    /**
     * An unfortunate construct of static classes I'm afraid. This really could be more elegant.
     */
    private static void resetHeaderRowIndexValues()
    {
        EMAResourceValidator.dataStartsRowIndex = -1;
        EMAResourceValidator.columnHeaderRowIndex = -1;
        EMAResourceValidator.sectionHeaderRowIndex = -1;
        EMAResourceValidator.lastCommentRowIndex = -1;
        EMAResourceValidator.firstNonEmptyRow = -1;
    }

    /**
     * @param unsplitInputLine The line in the input source that contains all of the critical column header values
     * @return A neat split list of column header values
     */
    private static ArrayList<String> getColumnHeaderValues(String unsplitInputLine)
    {
        ArrayList<String> columnHeaderValues = new ArrayList<String>();
        for(String currentColumnHeaderValue : unsplitInputLine.split(EMAResourceValidator.SPLIT_CHAR))
        {
            // Replace hyphen (-) with underscore (_)
            currentColumnHeaderValue = currentColumnHeaderValue.replace("-", "_").trim();
            columnHeaderValues.add(currentColumnHeaderValue);            
        }
        return columnHeaderValues;
    }

    /**
     * Searches the first 10 lines of input to find all of the critical locations where input header data is stored and located in this specific input source
     * find out what row the actual data starts on in the sheet,   EMAWorkbookValidator.dataStartsRowIndex
     * find out what row the main header values are located on,    EMAWorkbookValidator.columnHeaderRowIndex
     * find out what row the section header values are located on, EMAWorkbookValidator.sectionHeaderRowIndex
     * find out what row the last comment row is,                  EMAWorkbookValidator.lastCommentRowIndex
     * find out what row is the first non empty row,               EMAWorkbookValidator.firstNonEmptyRow
     * @param columnHeaderRows The first 10 rows of input from the input source to be validated. One row per entry in the ArrayList
     * @param originatingSheetName The sheet name that is currently being validated. Used to throw back to the user in an error if data can't be successfully located or the structure of the input data can't be understood.
     */
    private static void findHeaderRowIndexes(ArrayList<String> columnHeaderRows, String originatingSheetName)
    {
        Iterator<String> rowIterator = columnHeaderRows.listIterator();
        int rowIndex = 0;
        boolean foundNonBlankRow = false;
        while(rowIterator.hasNext())
        {
            String currentRowContents = rowIterator.next();
            if(!foundNonBlankRow && EMAResourceValidator.hasActualStringContent(currentRowContents))
            {
                foundNonBlankRow = true;
                EMAResourceValidator.firstNonEmptyRow = rowIndex;
            }
            if(EMAResourceValidator.isCommentRow(currentRowContents))
                EMAResourceValidator.lastCommentRowIndex = rowIndex;
            if(EMAResourceValidator.isSectionHeaderRow(currentRowContents))
                EMAResourceValidator.sectionHeaderRowIndex = rowIndex;

            rowIndex++;
        }

        if(EMAResourceValidator.sectionHeaderRowIndex != -1) // if the section headers are located, then the column headers come after them
            EMAResourceValidator.columnHeaderRowIndex = EMAResourceValidator.sectionHeaderRowIndex + 1;
        else if(EMAResourceValidator.firstNonEmptyRow != -1) // otherwise the column headers should be the first non empty row
            EMAResourceValidator.columnHeaderRowIndex = EMAResourceValidator.firstNonEmptyRow;
        else // if we can't locate the column headers, we can't define the EMA spec, so error out
            throw new IllegalArgumentException("Column header row cannot be deduced as no section headers could be found and no non empty rows could be found. Please fix or remove the offending sheet in question and revalidate: " + originatingSheetName);

        if(EMAResourceValidator.lastCommentRowIndex != -1) // if the last comment row is located, then the data should start immediately following the last comment row
            EMAResourceValidator.dataStartsRowIndex = EMAResourceValidator.lastCommentRowIndex + 1;
        else if(EMAResourceValidator.columnHeaderRowIndex != -1) // if there are no comment rows located, then the data should start after the column header definitions
            EMAResourceValidator.dataStartsRowIndex = EMAResourceValidator.columnHeaderRowIndex + 1;
        else // if we can't locate the exact row that information in the sheet starts on, then error out
            throw new IllegalArgumentException("Data starts row index cannot be deduced as no comment rows could be found and no column header row index could be found. Please fix or remove the offending sheet in question and revalidate: " + originatingSheetName);

        if(EMAResourceValidator.firstNonEmptyRow == -1) // if we don't find any data in the first set of rows that we parse, then the format is most likely invalid
            throw new IllegalArgumentException("No non empty rows could be found on the beginning of the input sheet. Please verify the sheet contents and submit again. Please fix or remove the offending sheet in question and revalidate: " + originatingSheetName);
    }

    /**
     * @param currentRowContents The current header row being analyzed
     * @return True if the string contains ANY of the values from EMASpec.LIST_OF_STARTING_HEADER_STRINGS, False otherwise
     */
    private static boolean isSectionHeaderRow(String currentRowContents)
    {
        currentRowContents = currentRowContents.toLowerCase(Locale.ENGLISH);
        for(String currentHeaderValue : AbstractEMASpec.LIST_OF_SECTION_HEADER_STRINGS)
            if(currentRowContents.contains(currentHeaderValue))
                return true;
        return false;
    }

    /**
     * @param currentRowContents The current header row being analyzed
     * @return True if the first value in the row starts with //, False otherwise
     */
    private static boolean isCommentRow(String currentRowContents)
    {
        return currentRowContents.startsWith("//");
    }

    /**
     * This should in essence verify whether or not the input string itself has any kind of important cell data inside of it or not.
     * This fixes the case where an empty row is parsed in and contains only separating characters.
     * @param inputString The input string to verify. Should be cell contents in string form separated by EMASpec.SPLIT_CHAR
     * @return True if the input string needs to be validated and contains actual content, false if not.
     */
    public static boolean hasActualStringContent(String inputString)
    {
        return StringUtils.containsAny(inputString, "etaoinshrdlcumwfgypbvkjxqz0123456789"); // this should be faster performance wise than previous method as no writes take place to the input string
    }

    /**
     * Retrieve the first 10 rows of the input sheet and return them as an ArrayList of strings with EMAWorkbookValidator.SPLIT_CHAR between them
     * @param inputWorkSheet The input sheet to validate
     * @return Strings containing each value from the row with EMAWorkbookValidator.SPLIT_CHAR between them
     */
    private static ArrayList<String> getColumnHeaderRows(Sheet inputWorkSheet)
    {
        ArrayList<String> headerRowContents = new ArrayList<String>();
        Iterator<Row> rowIterator = inputWorkSheet.rowIterator();
        int rowsRetrieved = 0;
        while(rowIterator.hasNext() && rowsRetrieved < 10) // read hopefully up to 10 lines to search for the EMA header rows
        {
            headerRowContents.add(EMAResourceValidator.getRowContents(rowIterator.next()));
            rowsRetrieved++;
        }
        return headerRowContents;
    }

    /**
     * Retrieve the first 10 rows of the input CSV file and return them as an ArrayList of strings with EMAWorkbookValidator.SPLIT_CHAR between each value in the row
     * @param csvRecords The iterable list of records obtained from the Apache Commons CSV Parse API
     * @return Up to the first 10 rows of data as strings wrapped in an ArrayList each containing EMAWorkbookValidator.SPLIT_CHAR between every individiaul value
     */
    private static ArrayList<String> getColumnHeaderRows(List<CSVRecord> csvRecords)
    {
        ArrayList<String> headerRowContents = new ArrayList<String>();

        for (CSVRecord currentRecord : csvRecords)
        {
            if(headerRowContents.size() == NUM_ROWS_FOR_HEADER_SEARCH) // take only the first 10 lines, or less, if there are less
                return headerRowContents;

            StringBuilder currentLine = new StringBuilder();

            for(int x = 0; x < currentRecord.size(); x++)
                currentLine.append(currentRecord.get(x) + EMAResourceValidator.SPLIT_CHAR);

            headerRowContents.add(currentLine.toString());
        }
        return headerRowContents;
    }

    /**
     * Takes in the current row's Row object as retrieved from a Row object's iterator implementation
     * Every value from the row will be iterated over and missing values will be represented via the empty string
     * @param currentRow The Row object returned from Apache POI Row.rowIterator().next() object's call.
     * @return A concatenated string object containing each value from the row represented by the input cell iterator object with each value separated by the EMASpec.SPLIT_CHAR value.
     */
    public static String getRowContents(Row currentRow)
    {
        StringBuilder rowContents = new StringBuilder();
        Cell currentCell = null;
        String cellContents = "";
        int maxColumnNumber = AbstractEMASpec.MAX_REASONABLE_COLUMN_COUNT < currentRow.getLastCellNum() ? AbstractEMASpec.MAX_REASONABLE_COLUMN_COUNT : currentRow.getLastCellNum();
        
        
        for(int cellNumber = 0; cellNumber < maxColumnNumber; cellNumber++)
        {
            currentCell = currentRow.getCell(cellNumber, Row.CREATE_NULL_AS_BLANK);
            cellContents = EMAResourceValidator.getCellContentsAsString(currentCell); // translate the cell's contents into string format, performing basic data sanitization in the process
            rowContents.append(cellContents);
            rowContents.append(EMAResourceValidator.SPLIT_CHAR); // cell marker, for split later
        }

        if(rowContents.length() > 0) // if we've succeeded in parsing a value from the row
            rowContents.setLength(rowContents.length() - 1); // delete extra character added to the end of the last loop during the last iterator of the above while loop
        return rowContents.toString(); // return the row contents as a string value
    }

    /**
     * Translates a line of file CSV file input into separated values for iteration over later
     * @param currentRow The current row being parsed from the CSV file input as an unparsed CSV formatted String
     * @return A concatenated string object containing each value from the row represented by the input cell iterator object with each value separated by the EMASpec.SPLIT_CHAR value.
     */
    public static String getRowContents(CSVRecord currentRecord)
    {
        StringBuilder rowContents = new StringBuilder();

        for(int x = 0; x < currentRecord.size(); x++)
            rowContents.append(currentRecord.get(x) + EMAResourceValidator.SPLIT_CHAR);

        if(rowContents.length() > 0) // if we've succeeded in parsing a value from the row
            rowContents.setLength(rowContents.length() - 1); // delete extra character added to the end of the last loop during the last iterator of the above while loop
        return rowContents.toString(); // return the row contents as a string value
    }

    /**
     * Utilizes the Apache POI API in order to successfully retrieve the current cell's contents along with its appropriate java object representation.
     * This function massages and converts all values to their toString() representation for the purpose of java regex matching regardless of original primitive type.
     * @param inputCurrentCell The current cell to being iterated over / parsed / validated.
     * @return A string representation of that cell's contents, or the empty string for cases of empty cells.
     */
    public static String getCellContentsAsString(Cell inputCurrentCell)
    {
        if(inputCurrentCell.getCellType() == Cell.CELL_TYPE_STRING) // if the current cell represents a string value
            return inputCurrentCell.getStringCellValue().replace(EMAResourceValidator.SPLIT_CHAR, "_"); // replace instances of the splitting character so no extra columns added later
        if(inputCurrentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) // if the current cell represents a number value
        {
            String cellContents;
            if(HSSFDateUtil.isCellDateFormatted(inputCurrentCell)) {
                Date date = inputCurrentCell.getDateCellValue();
                String timeTest = String.valueOf(new SimpleDateFormat("HH:mm").format(date));
                // This is a little weird, but I am using the HH:mm as a way to distinguish between
                // TotalRunTime column and Start/End/other dated columns.
                if(timeTest.equals("00:00"))
                {
                    // Dates columns
                    cellContents = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
                }
                else
                {
                    // RunTime
                    cellContents = timeTest;
                }
            } else {
                cellContents = String.valueOf(inputCurrentCell.getNumericCellValue()); // get the string representation of the number contained in the cell
                if (cellContents.length() > 2 && cellContents.endsWith(".0")) // turns non remainder floats into whole numbers to match regexes better. Turns 10.0 into 10 so decimals are not treated as floats when regex matching
                    cellContents = cellContents.substring(0, cellContents.length() - 2);
            }
            return cellContents;
        }
        if(inputCurrentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) // if the current cell represents a boolean value
            return String.valueOf(inputCurrentCell.getBooleanCellValue());
        if(inputCurrentCell.getCellType() == Cell.CELL_TYPE_BLANK) // if the current cell is blank according to the cell
            return "";
        else
            return inputCurrentCell.toString();
    }

    /**
     * Does its best to deduce which version of EMA that is represented by the input source.
     * First compares the number of input header values against the number of header values in each EMA spec. If an exact match is found, returns that EMA version.
     * Second compares the set of input header values against the set of unique column headers in each EMA spec. If a unique column header value is found, returns its encompassing EMA version.
     * Third
     * @param columnHeaderDefinitions The list of column header values that were previously located in the input source
     * @return A concrete EMAVersion definition which represents this program's best guess at determining the EMA version of the input sheet.
     */
    private static EMAVersion getEMAVersionNumber(ArrayList<String> columnHeaderDefinitions)
    {
        // See if we have the exact number of columns that we expect
        if(columnHeaderDefinitions.size() == EMASpec17TV.NUM_COLUMNS)
            return EMAVersion.EMASpec17TV;
        else if(columnHeaderDefinitions.size() == EMASpec17.NUM_COLUMNS)
            return EMAVersion.EMASpec17;
        else if(columnHeaderDefinitions.size() == EMASpec16TV.NUM_COLUMNS)
            return EMAVersion.EMASpec16TV;
        else if(columnHeaderDefinitions.size() == EMASpec16.NUM_COLUMNS)
            return EMAVersion.EMASpec16;
        else if(columnHeaderDefinitions.size() == EMASpec15.NUM_COLUMNS)
            return EMAVersion.EMASpec15;
        else if(columnHeaderDefinitions.size() == EMASpec14.NUM_COLUMNS)
            return EMAVersion.EMASpec14;
        System.out.println("Unexact number of column headers hit. Attempting to deduce by unique column names");

        // check for unique column names to help deduce if column numbering is off
        for(String currentUniqueHeaderDefinition : EMASpec17TV.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec17TV;
        }
        
        for(String currentUniqueHeaderDefinition : EMASpec17.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec17;
        }
        
        for(String currentUniqueHeaderDefinition : EMASpec16TV.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec16TV;
        }
        
        for(String currentUniqueHeaderDefinition : EMASpec16.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec16;
        }
        
        for(String currentUniqueHeaderDefinition : EMASpec15.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec15;
        }
        
        for(String currentUniqueHeaderDefinition : EMASpec14.UNIQUE_COLUMN_HEADER_VALUES)
        {
            if(columnHeaderDefinitions.contains(currentUniqueHeaderDefinition))
                return EMAVersion.EMASpec14;
        }
        
        System.out.println("No unique column names found. Using best guess for range of column sizes");

        // as a last resort, check a range of column numbers hoping that they haven't added any
        if(columnHeaderDefinitions.size() <= EMASpec14.NUM_COLUMNS + EMAResourceValidator.COLUMN_HEADER_COUNT_MAX_DIFF)
            return EMAVersion.EMASpec14;
        else if(columnHeaderDefinitions.size() <= EMASpec15.NUM_COLUMNS + EMAResourceValidator.COLUMN_HEADER_COUNT_MAX_DIFF)
            return EMAVersion.EMASpec15;
        else if(columnHeaderDefinitions.size() <= EMASpec16.NUM_COLUMNS + EMAResourceValidator.COLUMN_HEADER_COUNT_MAX_DIFF)
            return EMAVersion.EMASpec16;
        else if(columnHeaderDefinitions.size() <= EMASpec16TV.NUM_COLUMNS + EMAResourceValidator.COLUMN_HEADER_COUNT_MAX_DIFF)
            return EMAVersion.EMASpec16TV;
        else if(columnHeaderDefinitions.size() <= EMASpec17TV.NUM_COLUMNS + EMAResourceValidator.COLUMN_HEADER_COUNT_MAX_DIFF)
            return EMAVersion.EMASpec17TV;
        else
            System.out.println("Defaulting to 1.6. No version could reasonably be deduced from the input column header definitions.");
            return EMAVersion.EMASpec16;
    }

    /**
     * Used to determine whether the current input sheet is valid and therefore where it should be validated or not.
     * @param sheetName The name of the input sheet from the input source
     * @return True if the sheet should be validated, false otherwise
     */
    private static boolean isValidSheetName(String sheetName)
    {
        if(sheetName.toLowerCase(Locale.ENGLISH).contains("dictionary") ||
           sheetName.toLowerCase(Locale.ENGLISH).contains("faq")        ||
           sheetName.toLowerCase(Locale.ENGLISH).contains("instructions"))
            return false;
        return true;
    }
    
    private static ByteArrayOutputStream copyInputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        try
        {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return baos;
    }
}
