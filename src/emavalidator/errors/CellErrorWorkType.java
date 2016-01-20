package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorWorkType extends AbstractErrorEntry
{
    public static final String EXPECTED_WORK_TYPE = "This column is not optional. Expected: Movie, Short, Season, Episode";

    public CellErrorWorkType(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
    }

}
