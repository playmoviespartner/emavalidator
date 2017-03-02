package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorHDR extends AbstractErrorEntry
{
    public static String EXPECTED_HDR_VALUES = "True, False, DV, HDR10";
    
    public CellErrorHDR(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
