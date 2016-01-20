package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorTier extends AbstractErrorEntry
{
    public static final String EXPECTED_TIER_VALUES = "1, 13, A, CBA, Tier 1, Tier1, Tier-1, TierB, Tier-B, T 1";

    public CellErrorTier(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
