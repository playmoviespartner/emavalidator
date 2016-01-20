package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorSeasonNumber extends AbstractErrorEntry
{
    public static final String EXPECTED_VALUES_SEASON_NUMBER = "Season numbers must be a valid whole number only. E.G. 1, 25";

    public CellErrorSeasonNumber(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
