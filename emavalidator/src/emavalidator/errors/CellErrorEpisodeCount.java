package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorEpisodeCount extends AbstractErrorEntry
{
    public static final String EXPECTED_VALUES_EPISODE_COUNT = "A whole number only representing the number of paid episodes contained within the season.";

    public CellErrorEpisodeCount(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
