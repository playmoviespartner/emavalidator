package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorFormatProfile extends AbstractErrorEntry
{
    public static final String FORMAT_PROFILE_EMPTY_ERROR_MESSAGE = "FormatProfile cannot be blank."; 
    public static String EXPECTED_FORMAT_PROFILE_VALUES = "HD, SD, 3D, 3DHD, 3DSD, HFR, 3DHFR, 4K, 3D4K";
    
    public CellErrorFormatProfile(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
