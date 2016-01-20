/* Copyright 2014 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package emavalidator.validators;

import java.util.HashMap;

import emavalidator.AbstractEMASpec;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.End;
import emavalidator.columns.EntryType;
import emavalidator.columns.FormatProfile;
import emavalidator.columns.LicenseType;
import emavalidator.columns.Start;
import emavalidator.columns.TitleInternalAlias;
import emavalidator.errors.CellErrorDateValue;
import emavalidator.errors.CellErrorFormatProfile;
import emavalidator.errors.CellErrorLicenseType;
import emavalidator.errors.RowErrorEntryType;
import emavalidator.notifications.RowNotificationEntryType;

/* 
 * This RowValidator only used in EMASpec 1.5 and 1.6
 */
public class RowValidatorEntryType extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;

    public RowValidatorEntryType(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {

        try {
            String entryType = rowValues.get(EntryType.class.getSimpleName());
            String licenseType = rowValues.get(LicenseType.class.getSimpleName());
            String formatProfile = rowValues.get(FormatProfile.class.getSimpleName());
            String startDate = rowValues.get(Start.class.getSimpleName());
            String endDate = rowValues.get(End.class.getSimpleName());
            String title = rowValues.get(TitleInternalAlias.class.getSimpleName());
            // PriceType and PriceValue are checked in their own RowValidator.
            //String priceType = rowValues.get(PriceType.class.getSimpleName());
            //String priceValue = rowValues.get(PriceValue.class.getSimpleName());


            if (entryType.equals(EntryType.FULL_DELETE)) {
                // Full Delete ignores the following columns: 
                // License Type, Format Profile, Start, End, Price Type, Price Value
                // Skip validating. 
                
                // Notify that the EntryType is FullDelete in case that the FullDelete entry is accidental.
                ErrorLog.appendNotification(new RowNotificationEntryType(rowNumber, 
                                            RowNotificationEntryType.FULL_DELETE_NOTIFICATION_MESSAGE, 
                                            title,
                                            RowNotificationEntryType.FULL_DELETE_NOTIFICATION_DETAILS));

            } else if (entryType.equals(EntryType.FULL_EXTRACT)) {
                // For Full Extract, make sure values are available. If any values are empty, error out.
                if(licenseType.isEmpty()) {
                    // There are different accepted value messages based on EMA Spec version. 
                    if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec15) {
                        ErrorLog.appendError(new RowErrorEntryType(rowNumber, CellErrorLicenseType.LICENSE_TYPE_EMPTY_ERROR_MESSAGE,
                                licenseType, CellErrorLicenseType.VALUES_ACCEPTED_14_15, ErrorLevel.ERROR));                        
                    } else if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16 || 
                              this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV) {
                        ErrorLog.appendError(new RowErrorEntryType(rowNumber, CellErrorLicenseType.LICENSE_TYPE_EMPTY_ERROR_MESSAGE,
                                licenseType, CellErrorLicenseType.VALUES_ACCEPTED_16, ErrorLevel.ERROR));
                    }
                    return false;
                } else if (formatProfile.isEmpty()) {
                    ErrorLog.appendError(new RowErrorEntryType(rowNumber, CellErrorFormatProfile.FORMAT_PROFILE_EMPTY_ERROR_MESSAGE,
                                                               formatProfile, CellErrorFormatProfile.EXPECTED_FORMAT_PROFILE_VALUES, ErrorLevel.ERROR));
                    return false;
                } else if (startDate.isEmpty()) {
                    ErrorLog.appendError(new RowErrorEntryType(rowNumber, CellErrorDateValue.START_DATE_EMPTY_ERROR, 
                                                               startDate, CellErrorDateValue.EXPECTED_VALUES, ErrorLevel.CRITICAL));
                    return false;
                } else if (endDate.isEmpty()) {
                    ErrorLog.appendError(new RowErrorEntryType(rowNumber, CellErrorDateValue.END_DATE_EMPTY_ERROR, 
                                                               endDate, CellErrorDateValue.EXPECTED_VALUES, ErrorLevel.CRITICAL));
                    return false;
                } 
                // PriceType and PriceValue are checked in their own RowValidator.
//                else if (priceType.isEmpty()) {
//                    ErrorLog.appendError(new RowErrorTierOrPrice(rowNumber, RowErrorTierOrPrice.EMPTY_PRICE_TYPE,
//                                                                 priceValue, RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
//                    return false;
//                } else if (priceValue.isEmpty()) {
//                    ErrorLog.appendError(new RowErrorTierOrPrice(rowNumber, RowErrorTierOrPrice.EMPTY_PRICE_TYPE,
//                                                                 priceValue, RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
//                    return false;
//                }
            }
        } catch (NullPointerException NPE) {
            return true;
        }
        return true;
    }

}
