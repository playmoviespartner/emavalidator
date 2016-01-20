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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * An instance of an EMASpec contains all the version specific information for each EMA spec definition.
 * This abstract EMASpec definition contains all version agnostic information for each EMA spec definition.
 * EMASpec's main job is performing validation and more importantly reusing the validation code across each EMA version.
 * EMASpec's secondary job is acquiring an instance of the appropriate EMA version based off of an input string from the caller.
 * @author canavan
 */
public abstract class AbstractEMASpec
{
    /**
     * The package name that is concatenated before the class name to instantiate based on a string for reflection
     */
    public static final String PACKAGE_INFO = "emavalidator.";                // the string to prepend to any instance of EMASpec in order to instantiate via reflection using its fully qualified name

    /**
     * The list of all section headers defined across all EMA versions. Is used to try to find the section headers row during validation time.
     */
    public static final ArrayList<String> LIST_OF_SECTION_HEADER_STRINGS = new ArrayList<String>(Arrays.asList("disposition", "availterms", "availtrans", "availtrans", "availasset", "availmetadata", "avail Asset"));

    /**
     * The maximum number of columns that we want to reasonably read from the input source. This is to prevent reading cell values in very high numbers that could be defined but most likely useless.
     */
    public static final int MAX_REASONABLE_COLUMN_COUNT = 65;

    /**
     * The set of columns that represent the current EMA spec being validated against. The values should be rearranged to match the user's input source.
     */
    protected ColumnSpec columnSpec = new ColumnSpec();

    /**
     * The row of header contents that originate from the EMA spec that this instance is based off of. Values are separated by tab characters.
     */
    protected ArrayList<String> specHeaderContents = new ArrayList<String>();

    /**
     * The set of row validators that are applied to each set of row contents for each input line from the input source. Used to find errors based off of multiple column values.
     */
    protected RowSpec rowSpec = new RowSpec();

    /**
     * @return The column spec that this EMA instance is currently using to validate against.
     */
    public ColumnSpec getColumnSpec() { return this.columnSpec; }

    /**
     * A simple enum representing all the (currently) possible EMA spec versions.
     * @author canavan
     */
    public static enum EMAVersion { EMASpec14, EMASpec15, EMASpec16, EMASpec16TV, EMASpec17, EMASpec17TV; };

    /**
     * Standard super() constructor that's called via each EMASpec's unique implementation.
     * Step 1 is to build the unique column spec for the EMA spec implementation. This column spec is used to validate column values at a cell level.
     * Step 2 is to build the appropriate header strings for the EMA spec version. This is used to locate columns based on their name in the input sheet.
     * Step 3 is to build the unique row spec for the EMA spec implementation. This row spec is used to validate entire rows at a contextual multi-cell row level.
     * When instantiation is complete, the returned instance of EMASpec will represent the complete set of guidelines for fully validating both atomic cell values
     * on a cell by cell basis and also validate cells with respect to values in other cells. E.G. avail start date < avail end date.
     * The internal column spec itself will also be correctly rearranged based off of the input source from the user.
     */
    protected AbstractEMASpec()
    {
        this.buildColumnSpec();
        this.buildHeaderStrings();
        this.buildRowSpec(); // generate the respective row spec based on the EMA type instantiation at run time
    }

    /**
     * Used for instantiating the appropriate EMA spec version based off of the enum definition defined in this class: EMAVersion.
     * Best used when instantiating EMA specs internally from Java code. Please refer to the plain constructor for more instantiation details.
     * @param emaVersion An EMASpec.EMAVersion value representing the EMA spec to instantiate.
     * @return An instance of the requested EMA spec with the appropriate parameters passed in.
     */
    public static AbstractEMASpec getInstance(AbstractEMASpec.EMAVersion emaVersion)
    {
        try
        {
            Class<?> emaInstanceClass = Class.forName(AbstractEMASpec.PACKAGE_INFO + emaVersion.toString()); // get the class object for the required EMA version based off of the enum's declared name value
            return (AbstractEMASpec) emaInstanceClass.newInstance(); // generate a new instance of EMASpec with the given parameter list, cast it to EMASpec, and return it
        }
        catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e)
        {
            System.out.println("Error invoking new EMA instance class: " + e.getClass().getSimpleName());
            System.out.println("Exception of type: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A much less safe wrapper function in order to attempt to instantiate EMA specs off of free-formed strings.
     * Best used when instantiating EMA specs externally from web code where variables are sent as Strings and can't have a more specific type
     * Please refer to the plain constructor for more instantiation details.
     * TODO(canavan) This is kinda weak...
     * @param emaVersion A raw string value that should equal one of EMASpec.EMAVersion value's .toString() return value.
     * @return An instance of the requested EMA spec with the appropriate parameters passed in.
     */
    public static AbstractEMASpec getInstance(String emaVersion)
    {
        for (EMAVersion currentVersion : EMAVersion.values())
            if(currentVersion.toString().compareTo(emaVersion) == 0)
                return AbstractEMASpec.getInstance(currentVersion);
        return null;
    }

    /**
     * Validates the input Apache POI Sheet object passed in against this EMASpec object's validation guidelines.
     * Errors are reported directly to the static ErrorLog class at validation time.
     * Implements the Apache POI XSSF WorkBook API: http://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFWorkbook.html
     * @param inputWorkSheet The current sheet to be validated. Caller should be SURE this is a sheet with EMA data on it.
     * @param dataStartsRowIndex the first row number to start validating on, 0th based.
     * @return The number of errors encountered during validation.
     */
    public int validate(Sheet inputWorkSheet, int dataStartsRowIndex)
    {
        System.out.println("received workbook to validate. currently inside EMASpec.validate(inputWorkSheet) with sheet: " + inputWorkSheet.getSheetName());
        Row currentRow = null;
        int currentRowNumber = 0;
        Iterator<Row> rowIterator = inputWorkSheet.iterator(); // iterator to grab every row in the sheet
        while (rowIterator.hasNext()) // iterate over every row in the sheet
        {
            currentRow = rowIterator.next(); // get the current row object
            currentRowNumber = currentRow.getRowNum();
            if(currentRowNumber < dataStartsRowIndex) // do not validate header rows
                continue;

            String rowContents = EMAResourceValidator.getRowContents(currentRow); // get the contents of the entire row as one clean string value with EMASpec.SPLIT_CHAR inserted between each value
            
            // TODO(ckha):
            // 1. Create concatenated UID String from relevant column values
            // 2. Add uid to a mapping
            // 3. If collision, flag as a duplicate. (Not an error, just a notification that a duplicate is found).
            // 4. Else, no duplicates are found.
            //String concatUID = EMAResourceValidator.getUIDRowContents(currentRow);
                    
            if(EMAResourceValidator.hasActualStringContent(rowContents)) // verify that the row isn't blank before trying to validate it. prevents errors on rows that are seen as 'active' in the sheet but don't actually contain data.
            {
                String[] cellContents = rowContents.split(EMAResourceValidator.SPLIT_CHAR); // split the string into an array of values to create an array parallel to the column spec's column definition array
                //TODO(ckha) make cell contents get parsed as an arraylist
                cellContents = guaranteeRowLength(cellContents);
                // this.getMaximumColumnCount() -- minimum
                // currentRow.getLastCellNum() -- maximum
                // AbstractEMASpec.MAX_REASONABLE_COLUMN_COUNT -- HARD maximum
                int lastColumn = Math.max(this.getMaximumColumnCount(), currentRow.getLastCellNum());
                if(lastColumn > AbstractEMASpec.MAX_REASONABLE_COLUMN_COUNT)
                    lastColumn = AbstractEMASpec.MAX_REASONABLE_COLUMN_COUNT;
                
                for(int currentColumnNumber = 0; currentColumnNumber < lastColumn; currentColumnNumber++)
                {
                    String currentCellContents = this.getCurrentCellContents(cellContents, currentColumnNumber); // attempt to get the current cell's string contents
                    AbstractColumnDefinition currentColumnDefinition = this.columnSpec.getColumnDefinitionAt(currentColumnNumber); // get the column definition which corresponds to the current column we are currently validating
                    this.rowSpec.addValue(currentColumnDefinition.getColumnName(), currentCellContents); // map the current column's name to the current value from this row. for use in row validation afterwards.
                    currentColumnDefinition.validateInput(currentCellContents, currentRowNumber, currentColumnNumber); // validate the current cell value
                }
                this.rowSpec.validateRow(currentRowNumber); // validate the contents of the row
                this.rowSpec.clearValues(); // clear the contents of the row spec to delete old values before next row validation
            }
        }
        return ErrorLog.getTotalErrorCount();
    }
    
    public String[] guaranteeRowLength(String[] array) {
        int maxColumns = this.getMaximumColumnCount();
        ArrayList<String> returnThis = new ArrayList<String>(Arrays.asList(array));
        while(returnThis.size() < maxColumns)
            returnThis.add("");
        return returnThis.toArray(new String[returnThis.size()]);
    }

    /**
     * Validates the input set of Apache CSV iterable CSVRecords against this EMASpec object's validation guidelines.
     * Errors are reported directly to the static ErrorLog class at validation time.
     * @param inputCsvRecords The records from the input file, each of which should represent one row of file input
     * @param dataStartsRowIndex the first row number to start validating on, 0th based.
     * @return The number of errors encountered during validation.
     */
    public int validate(List<CSVRecord> inputCsvRecords, int dataStartsRowIndex)
    {
        System.out.println("received CSV file to validate. currently inside EMASpec.validate(inputCsvRecords)");
        int currentRowNumber = 0;
        for(CSVRecord currentRecord : inputCsvRecords) // for every line in the input file
        {
            if(currentRowNumber < dataStartsRowIndex) // do not validate header rows
            {
                currentRowNumber++;
                continue;
            }

            String rowContents = EMAResourceValidator.getRowContents(currentRecord); // get the contents of the entire row as one clean string value with EMASpec.SPLIT_CHAR inserted between each value

            if(EMAResourceValidator.hasActualStringContent(rowContents)) // verify that the row isn't blank before trying to validate it. prevents errors on rows that are seen as 'active' in the sheet but don't actually contain data.
            {
                String[] cellContents = rowContents.split(EMAResourceValidator.SPLIT_CHAR); // split the string into an array of values to create an array parallel to the column spec's column definition array
                for(int currentColumnNumber = 0; currentColumnNumber < Math.min(this.getMaximumColumnCount(), currentRecord.size()); currentColumnNumber++)
                {
                    String currentCellContents = this.getCurrentCellContents(cellContents, currentColumnNumber); // attempt to get the current cell's string contents
                    AbstractColumnDefinition currentColumnDefinition = this.columnSpec.getColumnDefinitionAt(currentColumnNumber); // get the column definition which corresponds to the current column we are currently validating
                    this.rowSpec.addValue(currentColumnDefinition.getColumnName(), currentCellContents); // map the current column's name to the current value from this row. for use in row validation afterwards.
                    currentColumnDefinition.validateInput(currentCellContents, currentRowNumber, currentColumnNumber); // validate the current cell value
                }
                this.rowSpec.validateRow(currentRowNumber); // validate the contents of the row
                this.rowSpec.clearValues(); // clear the contents of the row spec to delete old values before next row validation
            }
            currentRowNumber++;
        }
        return ErrorLog.getTotalErrorCount();
    }

    /**
     * A smart method that will take in the current row's contents as an array and pull out the desired value for the current column index being validated.
     * Is smart enough to realize that if the current active cell's column index is greater than the number of actual values in the row content, an empty string will be returned.
     * @param cellContents An array of string values which each represent an atomic cell value from the spreadsheet
     * @param currentColumnNumber The index where the desired value is located at. Performs smart error checking.
     * @return The value located at the input cell contents at the given index. The empty string is returned for all invalid index cases or where the desired index is higher than the defined column spec's length for this EMASpec instance.
     */
    private String getCurrentCellContents(String[] cellContents, int currentColumnNumber)
    {
        if(currentColumnNumber >= cellContents.length || currentColumnNumber < 0)
            return "";
        else
            return cellContents[currentColumnNumber];
    }


    /**
     * Override with each concrete implementation of EMASpec.
     * The maximum number of columns in this EMA spec.
     * Examples are:
     * 1.4   - 31
     * 1.5   - 39
     * 1.6   - 44
     * 1.6TV - 59
     */
    public abstract int getMaximumColumnCount();

    /**
     * Override with each concrete implementation of EMASpec.
     * Defines the set of columns which represent the implementation of the EMASpec.
     * Columns should be added in the order they are defined in the EMA Spec's master spreadsheet template.
     * This is as easy as calling this.columnSpec.addColumnDefinition(new NewColumnHere()); for each column in the spec.
     * Please do not forget to perform validation on the number of column definitions added vs expected in the spec.
     * Please refer to EMASpec14 for an example implementation.
     */
    protected abstract void buildColumnSpec();

    /**
     * Override with each concrete implementation of EMASpec. Defines the set of Strings that make up the names of each of the column names in this EMA spec.
     * If exact matches to these headers are not found at validation time, a CellErrorHeaderProcessing error will be added to the ErrorLog.
     */
    protected abstract void buildHeaderStrings();

    /**
     * Override with each implementation of EMASpec. Defines the set of validators to be run at the row level for a concrete EMA spec implementation.
     * Row validation is important for making sure that certain values are correct with respect to values in other columns.
     * Examples of row validation would be Start < End date and also that PriceType matches PriceValue.
     */
    protected abstract void buildRowSpec();

    /**
     * Override with each implementation of EMASpec
     * @return The EMA version corresponding to this instantiation of EMASpec
     */
    public abstract AbstractEMASpec.EMAVersion getEMAVersion();
}