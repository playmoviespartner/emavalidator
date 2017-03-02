package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorWCG extends AbstractErrorEntry
{
    public static String EXPECTED_WCG_VALUES = "True, False";
    
    public CellErrorWCG(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
