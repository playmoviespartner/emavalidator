package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorHFR extends AbstractErrorEntry
{
    public static String EXPECTED_HFR_VALUES = "True, False";
    
    public CellErrorHFR(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
