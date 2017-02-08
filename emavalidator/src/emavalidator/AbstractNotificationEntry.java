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

import org.apache.commons.lang3.StringUtils;

/**
 * A class which represents a notification message that took place during validation.
 * Encompasses where the notice took place, the message describing the notice, and any further details to take action on the notice (if applicable).
 * @author ckha
 */
public abstract class AbstractNotificationEntry
{

    /**
     * The actual notification message to display back to the user. This should be detailed and why the notice was flagged.
     */
    private String notificationMessage = "";


    /**
     * The detailed message to display back to the user. This should be very detailed and tell the user further information about the notification.
     */
    private String notificationDetails = "";

    /**
     * The list of values encountered across the same column that all throw this specific notification. This is used to keep the list of notifications tidy when it starts getting large like 100+. Although some specific values will get hidden, some will be enumerated back to the user.
     */
    private ArrayList<String> notificationValues = new ArrayList<String>();

    /**
     * The list of coordinate pairs where this notification occurred. Sometimes only a set of row values as each row validation error spans multiple columns and in those cases the columns themselves are not enumerated.
     */
    private ArrayList<RowColPair> notificationLocations = new ArrayList<RowColPair>();

    /**
     * The amount of spaces to pad each header of the error output to make sure that the colons line up in log display vertically to make the output nice and clean (and OCD friendly!).
     */
    private static final int OUTPUT_PAD_LENGTH = 15;
    
    /**
     * The list of column headers and the best order in which to print them when using CSV log output for the ErrorLog.
     * TODO(ckha) Consider generating this programmatically
     */
//    public final static ArrayList<String> CSV_LOG_OUTPUT_HEADERS = new ArrayList<String>(Arrays.asList("Level", "Type", "Count", "Location(s)", "Value", "Error", "Expected"));

    /**
     * Instantiate a row validation notification with no column specifically set.
     * @param rowNumber The row number where this notification occurred during validation.
     * @param notificationMessage The message to return to the log / user regarding the notification.
     * @param extraValue Any extra (custom) value from the row to add along with the notification message.
     * @param notificationDetails The extra details describing a notification for user to take any action manually.
     */
    public AbstractNotificationEntry(int rowNumber, String notificationMessage, String extraValue, String notificationDetails)
    {
        this(rowNumber, -1, notificationMessage, extraValue, notificationDetails);
    }

    /**
     * Instantiate a column validation notification with a row and column specifically set.
     * @param rowNumber The row number where this notification occurred during validation.
     * @param columnNumber The column number where this error occurred during validation. 0 for row validation errors.
     * @param notificationMessage The message to return to the log / user regarding the notification.
     * @param extraValue Any extra (custom) value from the row to add along with the notification message.
     * @param notificationDetails The extra details describing a notification for user to take any action manually.
     */
    public AbstractNotificationEntry(int rowNumber, int columnNumber, String notificationMessage, String extraValue, String notificationDetails)
    {
        this.notificationLocations.add(new RowColPair(rowNumber, columnNumber));
        this.notificationMessage = notificationMessage;
        this.notificationValues.add(extraValue);
        this.notificationDetails = notificationDetails;
    }

    /**
     * @return The column number where this notification occurred
     */
    public int getNotificationColumnNumber()
    {
        if(this.notificationLocations.size() == 0)
            return -1;
        return this.notificationLocations.get(0).getColumnNumber();
    }

    /**
     * The standard toString() implementation that should be used.
     * Outputs in standard log format showing row, column (optional), notice, any values, and notice descriptions.
     * @param validatingSpec The column-order specific column spec definition that generated this notification during validation and was input by the user
     * @return A string in log format with monospacing that details a notification event.
     */
    public String toLogString(AbstractEMASpec validatingSpec)
    {
        //the following comments contain an EXAMPLE of what the error output SHOULD look like. Not what it will specifically look like each time.

        StringBuilder returnThis = new StringBuilder(100);
        returnThis.append(StringUtils.leftPad("NOTICE ", 10, ' '));
        returnThis.append(StringUtils.leftPad("at location(s): ", AbstractNotificationEntry.OUTPUT_PAD_LENGTH, ' '))
                  .append(this.getPrettyPrintStringOfNotificationLocations()).append(System.lineSeparator());
        
        returnThis.append('\t').append(this.notificationMessage).append(" - ")
                  .append(this.notificationDetails);
        
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
            //returnThis.append(this.errorLevel.toString()).append(',');

            //Type
            if(this.getNotificationColumnNumber() != -1)
                returnThis.append(validatingSpec.getColumnSpec().getColumnDefinitionAt(this.getNotificationColumnNumber()).getColumnName());
            
            returnThis.append(',')
                      .append(String.valueOf(this.notificationLocations.size())).append(',') //Count
                      .append(this.getPrettyPrintStringOfNotificationLocations()).append(',') //Location(s)
                      .append(this.getPrettyPrintStringOfNotificationValues()).append(','); //Value

            //Error
            if(StringUtils.isNotEmpty(this.notificationMessage))
                returnThis.append('\"'); returnThis.append(this.notificationMessage); returnThis.append('\"');
            returnThis.append(',');

            //Expected
            if(StringUtils.isNotEmpty(this.notificationDetails))
                returnThis.append('\"'); returnThis.append(this.notificationDetails); returnThis.append('\"');
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
     * Compares notification column number, details, and message. If all 3 are the same, then two notifications are considered identical. This is used to aggregate similar notifications.
     * @return True if philosophically two notification values represent the same kind of input mistake made by the operator. False otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof AbstractNotificationEntry)) // null check not necessary, instanceof returns false if obj == null
            return false;

        if (this == obj)
            return true;

        AbstractNotificationEntry other = (AbstractNotificationEntry) obj; // safe because instanceof check passed earlier

        if(this.getNotificationColumnNumber() != other.getNotificationColumnNumber()) // if the errors aren't in the same column
            return false;


        if (this.notificationDetails == null) // if their expected values aren't the same
        {
            if (other.notificationDetails != null)
                return false;
        }
        else if (!this.notificationDetails.equals(other.notificationDetails))
            return false;
        

        if (this.notificationMessage == null) // if their error messages aren't the same
        {
            if (other.notificationMessage != null)
                return false;
        }
        else if (!this.notificationMessage.equals(other.notificationMessage))
            return false;

        return true;
    }

    /**
     * @return The computed hash code that hsould match in ALL cases where a.equals(b)
     */
    @Override
    public int hashCode() {
        // don't forget to download the lib for this and fix the project setup:
        // http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/HashCodeBuilder.html
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).
                append(this.getNotificationColumnNumber()).
                append(this.notificationDetails).
                append(this.notificationMessage).
                toHashCode();
    }

    /**
     * @return The list of locations where this notification has occurred on. These 'locations' are Row and column pairs in the excel sheet where they occurred. See RowColPair for full documentation.
     */
    public ArrayList<RowColPair> getNotificationLocations() { return this.notificationLocations; }

    /**
     * @return The list of values that are used to describe some detail about this notification.
     */
    public ArrayList<String> getNotificationValues() { return this.notificationValues; }

    /**
     * @return A pretty formatted string which concatenates each entry in this.errorLocations with a nice separating character sequence.
     */
    public String getPrettyPrintStringOfNotificationLocations()
    {
        String separatingSequence = " ";

        StringBuilder returnThis = new StringBuilder();

        if(this.getNotificationColumnNumber() != -1) // if this is not a row-based notification and has a valid column number, then append it. Otherwise ignore the default column value of -1
            returnThis.append("Column: ").append(this.notificationLocations.get(0).getExcelCoordinatesColumn()).append(' ');

        returnThis.append("Row(s): "); // get ready to append the row values

        // Print rows, taking into account sequential row numbers.
        int previousNum = -1;
        int currNum = 0;
        boolean printDots = false;
        for(RowColPair currentRowColPair : this.notificationLocations)
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
                        returnThis.append("-").append(previousNum).append(",");
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
    public String getPrettyPrintStringOfNotificationValues()
    {
        if(this.notificationValues.size() == 0)
            return "";

        StringBuilder returnThis = new StringBuilder();

        if(this.notificationValues.size() == 1)
        {
            returnThis.append('\"').append(notificationValues.get(0)).append('\"');
            return returnThis.toString();
        }

        //String separatingSequence = " ";
        //for(String currentValue : this.notificationValues)
        //    returnThis.append('\"').append(currentValue).append('\"').append(separatingSequence); // surround the value in quotes to keep invisible characters from hiding

        //if(returnThis.length() > separatingSequence.length()) // trim the final separating sequence from the string builder since it's always appended AFTER and will be present after the final loop
        //    returnThis.setLength(returnThis.length() - separatingSequence.length());

        return returnThis.toString();
    }
    
    public String getPrettyPrintStringOfLocationsAndValuesPairs()
    {
        String separatingSequence = " ";

        StringBuilder returnThis = new StringBuilder();

        if(this.getNotificationColumnNumber() != -1) // if this is not a row-based notification and has a valid column number, then append it. Otherwise ignore the default column value of -1
            returnThis.append("Column: ").append(this.notificationLocations.get(0).getExcelCoordinatesColumn()).append(' ');

        returnThis.append("Row(s): "); // get ready to append the row values

        for(RowColPair currentRowColPair : this.notificationLocations)
        {
            returnThis.append('(');
            returnThis.append(currentRowColPair.getExcelCoordinatesRowAsString()).append(separatingSequence);
        }

        if(returnThis.length() > separatingSequence.length()) // trim the final separating sequence from the string builder since it's always appended AFTER and will be present after the final loop
            returnThis.setLength(returnThis.length() - separatingSequence.length());

        return returnThis.toString();
    }
    
    /**
     * Assimilate the notification values and notification locations of the given notification into this notification instance. This is used to aggregate similar notifications and keep the log short, succint, and neat.
     * @param newNotification The new notification that just occurred which should be assimilated by this NotificationEntry instance instead of being its own unique notification.
     */
    public void assimilateNotification(AbstractNotificationEntry newNotification)
    {
        this.notificationLocations.addAll(newNotification.getNotificationLocations());
        this.notificationValues   .addAll(newNotification.getNotificationValues()); // add all the values that this not-new error encountered
    }
    
    /**
     * Returns the maximum of the number of notification locations vs. the number of notification values which should indicate the total number of notification of this type that have occurred.
     * @return The number of times this specific notification has occurred.
     */
    public int getNotificationCount()
    {
        return Math.max(this.notificationLocations.size(), this.notificationValues.size());
    }
}