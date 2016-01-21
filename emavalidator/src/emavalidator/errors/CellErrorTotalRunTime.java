package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorTotalRunTime extends AbstractErrorEntry
{
    public static final String EXPECTED_TOTAL_RUN_TIME = "String time-format value: HH:MM:SS 0:32:21, 2:34:56, 12:34:56";
    public static final String TOTAL_RUN_TIME_ERROR = "Incorrect formatting for the total run time. It should be expressed in HH:MM:SS";

    public CellErrorTotalRunTime(int rowNumber, int columnNumber, String errorMessage, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, ErrorLevel.CRITICAL, actualValue, expectedValue);
    }

}
