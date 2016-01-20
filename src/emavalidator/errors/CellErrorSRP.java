package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorSRP extends AbstractErrorEntry
{
    public static final String EXPECTED_SRP_VALUES = "Whole or decimal numbers only 2, 12, 3.12, .851";

    public CellErrorSRP(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
