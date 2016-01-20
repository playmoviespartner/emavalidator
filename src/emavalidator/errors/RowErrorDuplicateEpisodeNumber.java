package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class RowErrorDuplicateEpisodeNumber extends AbstractErrorEntry
{
    public static final String DUPLICATE_EPISODE_FOUND_ERROR_MESSAGE = "DUPLICATE Episode Numbers found for the same Show and Season.";
    public static final String DUPLICATE_EPISODE_FOUND_ERROR_DETAILS = "Please remember to check that the episode numbers vary for different episodes and that they are correct within the season.";

    public RowErrorDuplicateEpisodeNumber(int rowNumber, String errorMessage, String actualValue, String expectedValue)
    {
        super(rowNumber, errorMessage, ErrorLevel.ERROR, actualValue, expectedValue);
    }
}
