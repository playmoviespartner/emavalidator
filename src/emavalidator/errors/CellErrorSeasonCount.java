package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorSeasonCount extends AbstractErrorEntry
{
    public static final String EXPECTED_VALUES_SEASON_COUNT = "A whole number only representing the number of paid seasons contained within the series";

    public CellErrorSeasonCount(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
