package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorLicenseType extends AbstractErrorEntry
{
    public static final String VERSION_SPECIFIC_ERROR = "Values for this column are version specific. Please verify your input values against the corresponding EMA template version and try again.";
    public static final String VALUES_ACCEPTED_14_15 = "Values accepted for this column and this version are: EST, VOD, SVOD";
    public static final String VALUES_ACCEPTED_16 = "Values accepted for this column and this version are: EST, VOD, SVOD, POEST";
    public static final String LICENSE_TYPE_EMPTY_ERROR_MESSAGE = "LicenseType cannot be blank.";
    
    public CellErrorLicenseType(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
