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
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * A class which represents an exact error that took place during validation.
 * Encompasses where the error took place, the severity of the error, the exact value, and what that value should've been.
 * @author canavan
 */
public abstract class AbstractErrorEntry
{
    /**
     * A simple enum to represent the severity of an error after it has occurred. Helpful for prioritizing when there are multiple errors.
     * @author canavan
     */
    public static enum ErrorLevel
    {
        /**
         * A Warning means that manual inspection should take place. Likely not an error that will create parsing issues.
         * Also not guaranteed to be a soft error. Should be more of a stylistic error or something that is bad practice.
         * Examples are extra metadata characters like tab or newline inside legitimate values.
         */
        WARNING,
        /**
         * An error that in most cases will product a soft error but won't halt at parse time. These are extremely common and can
         * add a lot of complication to parsing. These usually require manual intervention and repair of the avails.
         * Examples are incorrect formatting of values like a tier that is actually a price or an EIDR ID not in actual EIDR format.
         */
        ERROR,
        /**
         * The highest level of error that has to be avoided at all costs and which will absolutely halt parsing.
         * These issues prevent someone from correctly interpreting your input avails or the values in the avail itself.
         * Examples are incorrect or missing header columns or missing critical fields in a line item such as title, window, tier, quality, or offer type (VOD / EST).
         */
        CRITICAL
    }

    /**
     * The actual error message to display back to the user. This should be very detailed and tell the user exactly what is wrong with the value. This should also be set by the validator that threw the error as ErrorEntry itself doesn't have any context of the EMA spec and what could be right or wrong.
     */
    private String errorMessage = "";

    /**
     * The severity level of this error. This is also decided by each implementation of ErrorEntry OR set by the validator when constructing a new Error. Exact details of each level of severity are at ErrorEntry.ErrorLevel.
     */
    private ErrorLevel errorLevel = ErrorLevel.ERROR;

    /**
     * The expected value message to display back to the user. This should be very detailed and tell the user exactly what kind of values that this column was expecting. This should also be set by the validator that threw the error as ErrorEntry itself doesn't have any context of Columns or ColumnSpecs
     */
    private String expectedValue = "";

    /**
     * The list of values encountered across the same column that all throw this specific error. This is used to keep the list of errors tidy when it starts getting large like 100+. Although some specific values will get hidden, some will be enumerated back to the user.
     */
    private ArrayList<String> errorValues = new ArrayList<String>();

    /**
     * The list of coordinate pairs where this error occurred. Sometimes only a set of row values as each row validation error spans multiple columns and in those cases the columns themselves are not enumerated.
     */
    private ArrayList<RowColPair> errorLocations = new ArrayList<RowColPair>();

    /**
     * Half the number of errors to print before and after a set of ellipses to indicate that there are too many errors to return back to the user and to show that the output has been summarized. This value should be EXACTLY ErrorEntry.MAX_REASONABLE_ERRORS / 2.
     */
    //private static final int HALF_OVERFLOW_LOCATION_VALUES = 5;

    /**
     * The maximum number of 'reasonable' error locations that can be printed back to the user. This value should be EXACTLY 2 * ErrorEntry.HALF_OVERFLOW_ERRORS. If there's more than this many errors, then the output is summarized to prevent flooding.
     */
    //private static final int MAX_REASONABLE_LOCATION_VALUES = 10;

    /**
     * Half the number of errors to print before and after a set of ellipses to indicate that there are too many errors to return back to the user and to show that the output has been summarized. This value should be EXACTLY ErrorEntry.MAX_REASONABLE_ERROR_VALUES / 2.
     */
    private static final int HALF_OVERFLOW_ERROR_VALUES = 3;

    /**
     * The maximum number of 'reasonable' error values that can be printed back to the user. This value should be EXACTLY 2 * ErrorEntry.HALF_OVERFLOW_ERROR_VALUES. If there's more than this many errors, then the output is summarized to prevent flooding.
     */
    private static final int MAX_REASONABLE_ERROR_VALUES = 6;

    /**
     * The amount of spaces to pad each header of the error output to make sure that the colons line up in log display vertically to make the output nice and clean (and OCD friendly!).
     */
    private static final int OUTPUT_PAD_LENGTH = 15;

    /**
     * The list of column headers and the best order in which to print them when using CSV log output for the ErrorLog.
     * TODO(canavan) Consider generating this programmatically
     */
    public final static ArrayList<String> CSV_LOG_OUTPUT_HEADERS = new ArrayList<String>(Arrays.asList("Level", "Type", "Count", "Location(s)", "Value", "Error", "Expected"));

    /**
     * Instantiate a row validation error with no column specifically set.
     * @param rowNumber The row number where this error occurred during validation.
     * @param errorMessage The message to return to the log / user regarding why the error happened.
     * @param errorLevel The severity of the error that occurred.
     * @param actualValue The value itself that produced the error.
     * @param expectedValue The expected value that the validator was looking for. Can be a concrete value or regex.
     */
    public AbstractErrorEntry(String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        this(-1, -1, errorMessage, errorLevel, actualValue, expectedValue);
    }
    
    /**
     * Instantiate a row validation error with no column specifically set.
     * @param rowNumber The row number where this error occurred during validation.
     * @param errorMessage The message to return to the log / user regarding why the error happened.
     * @param errorLevel The severity of the error that occurred.
     * @param actualValue The value itself that produced the error.
     * @param expectedValue The expected value that the validator was looking for. Can be a concrete value or regex.
     */
    public AbstractErrorEntry(int rowNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        this(rowNumber, -1, errorMessage, errorLevel, actualValue, expectedValue);
    }

    /**
     * Instantiate a column validation error with a row and column specifically set.
     * @param rowNumber The row number where this error occurred during validation.
     * @param columnNumber The column number where this error occurred during validation. 0 for row validation errors.
     * @param errorMessage The message to return to the log / user regarding why the error happened.
     * @param errorLevel The severity of the error that occurred.
     * @param actualValue The value itself that produced the error.
     * @param expectedValue The expected value that the validator was looking for. Can be a concrete value or regex.
     */
    public AbstractErrorEntry(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        this.errorLocations.add(new RowColPair(rowNumber, columnNumber));
        this.errorMessage = errorMessage;
        this.errorLevel = errorLevel;
        this.errorValues.add(actualValue);
        this.expectedValue = expectedValue;
    }

    /**
     * @return The column number where this error occurred
     */
    public int getErrorColumnNumber()
    {
        if(this.errorLocations.size() == 0)
            return -1;
        return this.errorLocations.get(0).getColumnNumber();
    }
    
    /**
     * @return The row number where this error occured
     */
    public int getErrorRowNumber()
    {
        if(this.errorLocations.size() == 0)
            return -1;
        return this.errorLocations.get(0).getRowNumber();
    }

    /**
     * The standard toString() implementation that should be used.
     * Outputs in standard log format showing error level, row, column (optional), type, value, error, and expected.
     * @param validatingSpec The column-order specific column spec definition that generated this error during validation and was input by the user
     * @return A string in log format with monospacing that details an exact error event. Resembles JSON notation or protocol buffer format a bit.
     */
    public String toLogString(AbstractEMASpec validatingSpec)
    {
        //the following comments contain an EXAMPLE of what the error output SHOULD look like. Not what it will specifically look like each time.

        StringBuilder returnThis = new StringBuilder(100);
        // WARNING AT {
        returnThis.append(StringUtils.leftPad(this.errorLevel.toString(), 10, ' ')).append(" at {").append(System.lineSeparator());

        // if this isn't a row based error, then output the column specific information for the user
        if(this.getErrorColumnNumber() != -1 && this.getErrorRowNumber() != -1) 
        {
            returnThis.append('\t')
                      .append(StringUtils.leftPad("column name: ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(validatingSpec.getColumnSpec().getColumnDefinitionAt(this.getErrorColumnNumber()).getColumnName())
                      .append(System.lineSeparator());
        }
        // type: Row Validation Error
        // let them know that it was a row validation error and no single column is to blame
        else if (this.getErrorColumnNumber() == -1 && this.getErrorRowNumber() != -1)
        {
            returnThis.append('\t')
                      .append(StringUtils.leftPad("type: ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append("Row Validation Error")
                      .append(System.lineSeparator());
        }
        // Overall error.
        // No row nor column is to blame. This is an overall error
        else if (this.getErrorColumnNumber() == -1 && this.getErrorRowNumber() == -1)
        {
            
        }

        //error count: 22
        returnThis.append('\t')
                  .append(StringUtils.leftPad("error count: ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                  .append(String.valueOf(this.errorLocations.size()))
                  .append(System.lineSeparator());

        // cell value: "Random Column Name"
        if(this.getErrorColumnNumber() != -1 && this.getErrorRowNumber() != -1) 
        {
            //  locations: Row: 24 Column(s): 3, 6, 9, 22
            returnThis.append('\t')
                      .append(StringUtils.leftPad("location(s): ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(this.getPrettyPrintStringOfErrorLocations())
                      .append(System.lineSeparator());
            
            returnThis.append('\t')
                      .append(StringUtils.leftPad("cell value(s): ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(this.getPrettyPrintStringOfErrorValues())
                      .append(System.lineSeparator());            
        }
        // Values from multiple cells 
        else if (this.getErrorColumnNumber() == -1 && this.getErrorRowNumber() != -1)
        {
            //  locations: Row: 24 Column(s): 3, 6, 9, 22
            returnThis.append('\t')
                      .append(StringUtils.leftPad("location(s): ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(this.getPrettyPrintStringOfErrorLocations())
                      .append(System.lineSeparator());
            
            returnThis.append('\t')
                      .append(StringUtils.leftPad("value(s): ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(this.getPrettyPrintStringOfErrorValues())
                      .append(System.lineSeparator());  
        }
        else if (this.getErrorColumnNumber() == -1 && this.getErrorRowNumber() == -1)
        {
            returnThis.append('\t')
                      .append(StringUtils.leftPad("value(s): ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append(this.getPrettyPrintStringOfErrorValues())
                      .append(System.lineSeparator());  
        }
        
        //      error: "Unsupported column name for this EMA spec"
        returnThis.append('\t')
                  .append(StringUtils.leftPad("error: ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                  .append('\"'); returnThis.append(this.errorMessage); returnThis.append('\"')
                  .append(System.lineSeparator());

        //   expected: "A better column name, preferably one from the EMA Spec"
        if(StringUtils.isNotEmpty(this.expectedValue))
        {
            returnThis.append('\t')
                      .append(StringUtils.leftPad("expected: ", AbstractErrorEntry.OUTPUT_PAD_LENGTH, ' '))
                      .append('\"'); returnThis.append(this.expectedValue); returnThis.append('\"')
                      .append(System.lineSeparator());
        }

        returnThis.append("  }").append(System.lineSeparator());
        return returnThis.toString();
    }

    /**
     * The standard toString() implementation that should be used when the user requires a CSV formatted output.
     * Column ordering is defined by ErrorEntry.CSV_LOG_OUTPUT_HEADERS and the code below SHOULD MIRROR THAT
     * @param validatingSpec The column-order specific column spec definition that generated this error during validation and was input by the user
     * @return A string in csv format that details an exact error event.
     */
    public String toCSVString(AbstractEMASpec validatingSpec)
    {
            StringBuilder returnThis = new StringBuilder(100);

            //Level
            returnThis.append(this.errorLevel.toString()).append(',');

            //Type
            if(this.getErrorColumnNumber() != -1)
                returnThis.append(validatingSpec.getColumnSpec().getColumnDefinitionAt(this.getErrorColumnNumber()).getColumnName());
            
            returnThis.append(',')
                      .append(String.valueOf(this.errorLocations.size())).append(',') //Count
                      .append(this.getPrettyPrintStringOfErrorLocations()).append(',') //Location(s)
                      .append(this.getPrettyPrintStringOfErrorValues()).append(','); //Value

            //Error
            if(StringUtils.isNotEmpty(this.errorMessage))
                returnThis.append('\"'); returnThis.append(this.errorMessage); returnThis.append('\"');
            returnThis.append(',');

            //Expected
            if(StringUtils.isNotEmpty(this.expectedValue))
                returnThis.append('\"'); returnThis.append(this.expectedValue); returnThis.append('\"');
            returnThis.append(',');

            return returnThis.toString();
    }

    /**
     * Smart toString method which grabs contextual data from the defining column spec
     * @param outputLogType The type of log that is being generated.
     * @param validatingSpec The column-order specific column spec definition that generated this error during validation and was input by the user
     * @return An appropriately formatted log string based on the input log type
     */
    public String toString(ErrorLog.OUTPUT_LOG_TYPE outputLogType, AbstractEMASpec validatingSpec)
    {
        if(outputLogType == ErrorLog.OUTPUT_LOG_TYPE.CSV)
            return this.toCSVString(validatingSpec);
        else
            return this.toLogString(validatingSpec);
    }

    /**
     * Compares error levels, error message, expected value, and column number. If all 4 are the same, then two errors are considered identical. This is used to aggregate similar errors. This does NOT take VALUE into account.
     * @return True if philosophically two error values represent the same kind of input mistake made by the operator. False otherwise.
     */
    @Override
    //TODO(canavan) This is slow when the list of errors is very large. O(n^2) or close to it unfortunately. Unique error IDs would be awesome or a hash table.
    public boolean equals(Object obj)
    {
        if (!(obj instanceof AbstractErrorEntry)) // null check not necessary, instanceof returns false if obj == null
            return false;

        if (this == obj)
            return true;

        AbstractErrorEntry other = (AbstractErrorEntry) obj; // safe because instanceof check passed earlier

        if(this.getErrorColumnNumber() != other.getErrorColumnNumber()) // if the errors aren't in the same column
            return false;

        if (this.errorLevel != other.errorLevel) // if they're not the same level
            return false;

        if (this.expectedValue == null) // if their expected values aren't the same
        {
            if (other.expectedValue != null)
                return false;
        }
        else if (!this.expectedValue.equals(other.expectedValue))
            return false;
        

        if (this.errorMessage == null) // if their error messages aren't the same
        {
            if (other.errorMessage != null)
                return false;
        }
        else if (!this.errorMessage.equals(other.errorMessage))
            return false;

        return true;
    }

    /**
     * @return The level of severity of this ErrorEntry. Refer to ErrorEntry.ErrorLevel for full documentation.
     */
    public ErrorLevel getErrorLevel() { return this.errorLevel; }

    /**
     * @return The list of locations where this error has occurred on. These 'locations' are Row and column pairs in the excel sheet where they occurred. See RowColPair for full documentation.
     */
    public ArrayList<RowColPair> getErrorLocations() { return this.errorLocations; }

    /**
     * @return The list of error values that triggered this error to occur.
     */
    public ArrayList<String> getErrorValues() { return this.errorValues; }

    /**
     * @return A pretty formatted string which concatenates each entry in this.errorLocations with a nice separating character sequence.
     */
    public String getPrettyPrintStringOfErrorLocations()
    {
        String separatingSequence = " ";

        StringBuilder returnThis = new StringBuilder();

        if(this.getErrorColumnNumber() != -1) // if this is not a row-based error and has a valid column number, then append it. Otherwise ignore the default column value of -1
            returnThis.append("Column: ").append(this.errorLocations.get(0).getExcelCoordinatesColumn()).append(' ');

        returnThis.append("Row(s): "); // get ready to append the row values

/*
        // SAVE THIS CODE: Old printing method prints out the FIRST 5 and LAST 5 locations. 
        // We actually may want to be spammy when printing error locations and values.
        if(this.errorLocations.size() > AbstractErrorEntry.MAX_REASONABLE_LOCATION_VALUES) // if we've hit a TON of errors, we don't want to print all of them so we need to summarize them
        {
            for(int x = 0; x < AbstractErrorEntry.HALF_OVERFLOW_LOCATION_VALUES; x++) // append the first 5 error locations where this error occurred on
            {
                returnThis.append(this.errorLocations.get(x).getExcelCoordinatesRowAsString()).append(separatingSequence);
            }

            returnThis.append("  ...  "); // ellipses to emphasize there are locations between these two sets left unprinted

            for(int x = this.errorLocations.size() - AbstractErrorEntry.HALF_OVERFLOW_LOCATION_VALUES; x < this.errorLocations.size(); x++) // append the last 5 error locations.
            {
                returnThis.append(this.errorLocations.get(x).getExcelCoordinatesRowAsString()).append(separatingSequence);
            }
            
        }
        else { // if we haven't hit a ton of errors, just append all of the locations since less than 10 isn't too spammy
            for(RowColPair currentRowColPair : this.errorLocations)
            {
                returnThis.append(currentRowColPair.getExcelCoordinatesRowAsString()).append(separatingSequence);
            }
        } 

        if(returnThis.length() > separatingSequence.length()) // trim the final separating sequence from the string builder since it's always appended AFTER and will be present after the final loop
            returnThis.setLength(returnThis.length() - separatingSequence.length());
 */
        
        // Print rows, taking into account sequential row numbers.
        int previousNum = -1;
        int currNum = 0;
        boolean printDots = false;
        for(RowColPair currentRowColPair : this.errorLocations)
        {
            // Get the current row number
            currNum = currentRowColPair.getExcelCoordinatesRowAsInt();
            
            // Always prints out the very first row number to start off.
            if(previousNum == -1) 
            {
                returnThis.append(currNum);
            }
            else 
            {
                // Check if the current row is a consecutive row from the previous. If it is, we want to print the '-' to show consecutiveness.
                if(previousNum+1 == currNum) 
                {
                    printDots = true;
                }
                else 
                {
                    // Hide the consecutive row numbers in the '-' and also print the number that ends the consecutive count.
                    if(printDots) 
                    {
                        returnThis.append("-").append(previousNum);
                        printDots = false;
                    }
                    
                    // Print the current row number that we are checking.
                    returnThis.append(separatingSequence).append(currNum);
                }
            }
            // Set the previous number to be the current number, to be checked in the next iteration.
            previousNum = currNum;
        }
        // If the rows were consecutive all the way through, printDots will be true and fall through. Thus, print out the final row if printDots is true at the end.
        if(printDots) 
        {
            returnThis.append("-").append(previousNum);
        }
        
        return returnThis.toString();
    }

    /**
     * @return A pretty formatted string which concatenates each entry in this.errorValues with a nice separating character sequence.
     */
    public String getPrettyPrintStringOfErrorValues()
    {
        if(this.errorValues.size() == 0)
            return "";

        StringBuilder returnThis = new StringBuilder();

        if(this.errorValues.size() == 1)
        {
            returnThis.append('\"').append(errorValues.get(0)).append('\"');
            return returnThis.toString();
        }

        String separatingSequence = " ";

        if(this.errorValues.size() > AbstractErrorEntry.MAX_REASONABLE_ERROR_VALUES) // if we've hit a TON of errors, we don't want to print all of them so we need to summarize them
        {
            for(int x = 0; x < AbstractErrorEntry.HALF_OVERFLOW_ERROR_VALUES; x++) // append the first 5 error locations where this error occurred on
                returnThis.append('\"').append(this.errorValues.get(x)).append('\"').append(separatingSequence); // surround the value in quotes to keep invisible characters from hiding

            returnThis.append("  ...  "); // ellipses to emphasize there are locations between these two sets left unprinted

            for(int x = this.errorValues.size() - AbstractErrorEntry.HALF_OVERFLOW_ERROR_VALUES; x < this.errorValues.size(); x++) // append the last 5 error locations.
                returnThis.append('\"').append(this.errorValues.get(x)).append('\"').append(separatingSequence); // surround the value in quotes to keep invisible characters from hiding
        }
        else // if we haven't hit a ton of errors, just append all of the locations since less than 10 isn't too spammy
            for(String currentValue : this.errorValues)
                returnThis.append('\"').append(currentValue).append('\"').append(separatingSequence); // surround the value in quotes to keep invisible characters from hiding

        if(returnThis.length() > separatingSequence.length()) // trim the final separating sequence from the string builder since it's always appended AFTER and will be present after the final loop
            returnThis.setLength(returnThis.length() - separatingSequence.length());

        return returnThis.toString();
    }

    /**
     * Assimilate the error values and error locations of the given error into this error instance. This is used to aggregate similar errors and keep the log short, succint, and neat.
     * @param newError The new error that just occurred which should be assimilated by this ErrorEntry instance instead of being its own unique error
     */
    public void assimilateError(AbstractErrorEntry newError)
    {
        this.errorLocations.addAll(newError.getErrorLocations());
        this.errorValues   .addAll(newError.getErrorValues()); // add all the values that this not-new error encountered
    }
}