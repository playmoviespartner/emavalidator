package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorLicenseRightsDescription extends AbstractErrorEntry
{
    public static final String ERROR_MESSAGE_LICENSE_RIGHTS_DESCRIPTION = "Non-standard value: while not necessarily an issue, please ensure that standard content categories use enumerated LicenseRightsDescription values.";
    public static final String EXPECTED_LICENSE_RIGHTS_DESCRIPTION = "New Release, Library, Mega-Library, DD-Theatrical, Pre-Theatrical, Early EST, Preorder EST, Early VOD, Preorder VOD, Next Day TV, Season Only, DTV, DD-DVD, Free";

    public CellErrorLicenseRightsDescription(int rowNumber, int columnNumber, String errorMessage, ErrorLevel errorLevel, String actualValue,
            String expectedValue)
    {
        super(rowNumber, columnNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
