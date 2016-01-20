package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorPriceValue extends AbstractErrorEntry
{
    public static final String EXPECTED_PRICE_VALUE = "Either whole numbers, decimal numbers, valid license rights description values, tiers, or blank.";

    public CellErrorPriceValue(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }
}
