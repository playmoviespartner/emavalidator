package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorNGAudio extends AbstractErrorEntry
{
    public static String EXPECTED_NGAUDIO_VALUES = "True, False, Atmos, DTS:X, Auro3D";
    
    public CellErrorNGAudio(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
