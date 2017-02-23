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

import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.End;
import emavalidator.columns.EntryType;
import emavalidator.columns.Start;
import emavalidator.errors.RowErrorChronologicalDates;

public class RowValidatorStartLessEnd extends AbstractRowValidator
{
    public static final String EXPECTED_CHRONOLOGICAL = "Please be completely sure that the avail's start date is before and not equal to or after the avail's end date. Also make sure that your start and end dates are in the accepted format and values.";

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String      entryType = rowValues.get(EntryType.class.getSimpleName());
            String availStartDate = rowValues.get(Start.class.getSimpleName());
            String   availEndDate = rowValues.get(  End.class.getSimpleName());

            // Ignore validating Start and End if EntryType is "Full Delete"
            if(!entryType.equals(EntryType.FULL_DELETE)) {
                if(availStartDate == null ||
                        availEndDate == null)
                    return true; // do not validate start vs. end in the case where either field is missing. this only happens when input columns are improperly named
                
                // If Start and End dates are empty strings, 
                if(availStartDate.isEmpty() || availEndDate.isEmpty()) {
                    return false;
                }
                
                if(ValidatorUtils.areValidStartEndDates(availStartDate, availEndDate))
                    return true;
                else
                {
                    // We know two possibilities:
                    // (1) either Start or End is invalid. Fine-tune the checks here to ignore error message.
                    if (!ValidatorUtils.isValidStartEndDate(Start.class.getSimpleName(), availStartDate) ||
                            !ValidatorUtils.isValidStartEndDate(End.class.getSimpleName(), availEndDate))
                    {
                        return false;
                    }
                    
                    // (2) Start and End are valid, but they are not in chronological order.
                    // If the error is not caught above, then we know we have a chronological order error.
                    ErrorLog.appendError(new RowErrorChronologicalDates(
                            rowNumber,
                            RowErrorChronologicalDates.DATES_NOT_CHRONOLOGICAL,
                            availStartDate + " vs. " + availEndDate,
                            RowValidatorStartLessEnd.EXPECTED_CHRONOLOGICAL));
                }
                return false;                
            }
            return true;
        }
        catch (NullPointerException NPE) { return true; } // do not attempt to validate when column values are missing
    }
}
