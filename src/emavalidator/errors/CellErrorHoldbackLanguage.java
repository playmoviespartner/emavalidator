package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorHoldbackLanguage extends AbstractErrorEntry
{
    public static final String EXPECTED_HOLDBACK_VALUES = "ES, FR, BR, etc";

    public CellErrorHoldbackLanguage(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
