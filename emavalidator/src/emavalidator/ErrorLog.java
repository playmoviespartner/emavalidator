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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import emavalidator.AbstractErrorEntry.ErrorLevel;

/**
 * A fully static class that implements a global error log.
 * Any class from anywhere in the project should be able to throw and store an error as needed.
 * @author canavan
 */
public final class ErrorLog
{
    /**
     * The list of SheetErrorSummary instances: one for each input sheet from the input file. There should be only one instance for CSV files but N number from input workbooks.
     */
    private static ArrayList<SheetErrorSummary> sheetErrorSummaries = new ArrayList<SheetErrorSummary>(); // store each sheet's unique error list

    /**
     * The current SheetErrorSummary that all instances of new errors are redirected to and stored in. When a new sheet is being validated, this instance must be updated.
     */
    private static SheetErrorSummary currentSheetErrorSummary;

    /**
     * The preferred output log type when this ErrorLog is printed to screen, saved to file, or sent to a web front end, etc.
     */
    private static OUTPUT_LOG_TYPE outputLogType = OUTPUT_LOG_TYPE.LOG;

    /**
     * The type of log output desired when error values are printed to screen, saved to file, or sent to a web front end, etc.
     * @author canavan
     */
    public enum OUTPUT_LOG_TYPE
    {
        /**
         * A standard log output that resembles JSON notation a lot. Recommended and default log output type. Is also the cleanest and summarizes errors.
         */
        LOG,
        /**
         * The non standard CSV log output that prints clean CSV based notation with commas wrapping in values. Used to analyze and sort errors in a spreadsheet program if the user so desires.
         */
        CSV
    }

    private ErrorLog() { } // A fully static class needs no concrete constructor. This also correctly enforces that all future methods are static as well

    /**
     * Add a new error to this ErrorLog. Like errors from the same columns are joined instead of repeated. This saves output space but requires more calculation at run time.
     * @param newError Add a new error to store inside this error log inside the current error sheet summary.
     */
    public static void appendError(AbstractErrorEntry newError)  { currentSheetErrorSummary.appendError(newError); }

    /**
     * Add a new notification to this ErrorLog.
     * @param newNotification Add a new notification to store inside this error log inside the current error sheet summary.
     */
    public static void appendNotification(AbstractNotificationEntry newNotification) { currentSheetErrorSummary.appendNotification(newNotification); }

    /**
     * Print the output of each sheet error summary directly to the console
     */
    public static void printErrorLog()
    {
        for(SheetErrorSummary currentSheetErrorSummary : sheetErrorSummaries)
            currentSheetErrorSummary.printSheetErrorSummary();
    }

    /**
     * @return The total number of errors that occurred across all sheets for all types. Used to display a total error count at the end of validation time.
     */
    public static int getTotalErrorCount()
    {
        int totalErrorCount = 0;
        for(SheetErrorSummary currentSheetErrorSummary : sheetErrorSummaries)
            totalErrorCount += currentSheetErrorSummary.getErrorCount();
        return totalErrorCount;
    }
    
    /**
     * @return The total number of errors that occurred across all sheets for all types. Used to display a total error count at the end of validation time.
     */
    public static int getTotalErrorColumnsCount()
    {
        int totalErrorColumnsCount = 0;
        for(SheetErrorSummary currentSheetErrorSummary : sheetErrorSummaries)
            totalErrorColumnsCount += currentSheetErrorSummary.getErrorColumnsCount();
        return totalErrorColumnsCount;
    }

    /**
     * @return Each ErrorLevel and it's associated error count formatted in a pretty way with a line separator between each entry
     */
    public static String getErrorSummary()
    {
        StringBuilder combinedErrorSummary = new StringBuilder(100);
        int grandTotalErrors = 0;
        HashMap<ErrorLevel, Integer> errorMappingSummary = new HashMap<ErrorLevel, Integer>(); // create a mapping for the total number of errors per error type
        for(ErrorLevel currentErrorLevel : ErrorLevel.values()) // initialize the master error mapping to 0 for every error type
            errorMappingSummary.put(currentErrorLevel, 0);

        for(SheetErrorSummary currentSheetErrorSummary : sheetErrorSummaries) // for all the sheets currently validated so far
        {
            HashMap<ErrorLevel, Integer> currentSheetErrorSummaryErrorMapping = currentSheetErrorSummary.getErrorCounts(); // get the current mapping of errors for that sheet
            for(ErrorLevel currentErrorLevel : ErrorLevel.values()) // for every type of error level there is
                errorMappingSummary.put(currentErrorLevel, currentSheetErrorSummaryErrorMapping.get(currentErrorLevel) + errorMappingSummary.get(currentErrorLevel)); // take the current master error mapping count and add the current sheet error's count to it
        }

        for (ErrorLevel currentErrorLevel : errorMappingSummary.keySet()) {
            combinedErrorSummary.append("Level: " +  String.format("%8s", currentErrorLevel) + "\t" + "Count: " + errorMappingSummary.get(currentErrorLevel) + System.lineSeparator());
            grandTotalErrors += errorMappingSummary.get(currentErrorLevel);
        }
        
        // Append grand total
        combinedErrorSummary.append("Total Error Count: " + grandTotalErrors + System.lineSeparator());
        
        return combinedErrorSummary.toString();
    }

    /**
     * Erase all entries in the ErrorLog, effectively resetting it for the next validation attempt
     */
    public static void clearErrorLog()
    {
        System.out.println("clear error log");
        for(SheetErrorSummary currentSheetErrorSummary : ErrorLog.sheetErrorSummaries)
            currentSheetErrorSummary.clearErrorSheetSummary(); // just to be safe. these SHOULD get garbage collected by the next line
        ErrorLog.sheetErrorSummaries = new ArrayList<SheetErrorSummary>();
        ErrorLog.currentSheetErrorSummary = null; // drop the reference to the current sheet error summary. this should be the final garbage collection state
    }

    /**
     * Retrieves this ErrorLog's contents in String format by iterating over each internally stored instance of SheetErrorSummary and formatting their respective errors.
     * This is the master formatting function and should be used in almost all cases to retrieve correctly formatted error output.
     * @return A cleanly formatted string with exact details of each error that occurred as well as a workbook wide error summary AND individual worksheet error summaries
     */
    public static String getFormattedErrorLog()
    {
        StringBuilder formattedErrorLog = new StringBuilder();
        formattedErrorLog.append(System.lineSeparator()).append(ErrorLog.getErrorSummary());

        for(SheetErrorSummary currentSheetErrorSummary : sheetErrorSummaries)
        {
            ArrayList<AbstractErrorEntry> currentErrorList = currentSheetErrorSummary.getErrorLog();
            ArrayList<AbstractNotificationEntry> currentNotificationsList = currentSheetErrorSummary.getNotificationsLog();

            formattedErrorLog.append(System.lineSeparator()).append(currentSheetErrorSummary.getSheetPrintString()).append(System.lineSeparator());

            if(ErrorLog.outputLogType == ErrorLog.OUTPUT_LOG_TYPE.CSV)
                formattedErrorLog.append(ErrorLog.getCSVFormattedColumnHeaders()).append(System.lineSeparator());

            // Append Notifications to print Notifications Log
            for(AbstractNotificationEntry currentNotificationEntry : currentNotificationsList)
                formattedErrorLog.append(currentNotificationEntry.toString(ErrorLog.outputLogType, currentSheetErrorSummary.getValidatingEMASpec())).append(System.lineSeparator());
            
            formattedErrorLog.append(System.lineSeparator());
            
            // Append Errors to print Error Log
            for(AbstractErrorEntry currentErrorEntry : currentErrorList)
                formattedErrorLog.append(currentErrorEntry.toString(ErrorLog.outputLogType, currentSheetErrorSummary.getValidatingEMASpec())).append(System.lineSeparator());
        }
        return formattedErrorLog.toString();
    }

    /**
     * @return Returns the list of columns separated by commas in order to print to the CSV log output
     */
    private static String getCSVFormattedColumnHeaders()
    {
        StringBuilder csvColumnHeaderContents = new StringBuilder();
        for(String currentColumnHeader : AbstractErrorEntry.CSV_LOG_OUTPUT_HEADERS)
        {
            csvColumnHeaderContents.append(currentColumnHeader).append(',');
        }
        csvColumnHeaderContents.setLength(csvColumnHeaderContents.length()-1);
        csvColumnHeaderContents.append(System.lineSeparator());
        return csvColumnHeaderContents.toString();
    }

    /**
     * Instantiate a new SheetErrorSummary to store inside this ErrorLog.
     * This stores all the sheet-specific error information for the sheet being validated currently
     * @param sheetName The name of the sheet about to be validated
     * @param emaVersion The EMA version of the sheet about to be validated
     * @param validatingSpec The column-order specific column spec definition that was input by the user
     * @param sheetIndex The index of the sheet inside the Workbook about to be validated
     */
    public static void setCurrentSheet(String sheetName, AbstractEMASpec.EMAVersion emaVersion, AbstractEMASpec validatingSpec, int sheetIndex)
    {
        ErrorLog.currentSheetErrorSummary = new SheetErrorSummary(sheetName, sheetIndex, emaVersion, validatingSpec);
        sheetErrorSummaries.add(currentSheetErrorSummary);
    }

    /**
     * A string representing the kind of log the user would like to receive. Recommend using ErrorLog.OUTPUT_LOG_TYPE.xyz.toString() to set the value.
     * @param logOutputType A string containing "csv" to set to OUTPUT_LOG_TYPE.CSV or anything else for OUTPUT_LOG_TYPE.LOG
     */
    public static void setErrorLogType(String logOutputType)
    {
        if(logOutputType.toLowerCase(Locale.ENGLISH).contains("csv"))
            ErrorLog.outputLogType = ErrorLog.OUTPUT_LOG_TYPE.CSV;
        else
            ErrorLog.outputLogType = ErrorLog.OUTPUT_LOG_TYPE.LOG;
    }

    /**
     * @param logOutputType The desired log output type requested by the user. See ErrorLog.OUTPUT_LOG_TYPE for complete documentation.
     */
    public static void setErrorLogType(ErrorLog.OUTPUT_LOG_TYPE logOutputType)
    {
        ErrorLog.outputLogType = logOutputType;
    }
}
