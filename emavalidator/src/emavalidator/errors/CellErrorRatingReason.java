package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorRatingReason extends AbstractErrorEntry
{
    public static final String EXPECTED_RATING_REASON = "L, S, V, etc";

    public CellErrorRatingReason(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
