package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorEpisodeNumber extends AbstractErrorEntry
{
    public static final String EXPECTED_VALUES_EPISODE_NUMBER = "Episode numbers must be a valid whole number only.";
    
    public CellErrorEpisodeNumber(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
