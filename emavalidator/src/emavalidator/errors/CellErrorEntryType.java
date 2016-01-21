package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorEntryType extends AbstractErrorEntry
{
    public static final String VERSION_SPECIFIC_ERROR = "Values for this column are version specific. Please verify your input values against the corresponding EMA template version and try again.";
    public static final String VALUES_ACCEPTED_14 = "Values accepted for this column and this version are: Create, Update, Delete, Full Extract";
    public static final String VALUES_ACCEPTED_15_16 = "Values accepted for this column and this version are: Full Extract, Full Delete";
    
    public CellErrorEntryType(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
