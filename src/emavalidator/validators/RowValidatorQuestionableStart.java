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

import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.LicenseType;
import emavalidator.columns.ReleaseHistoryOriginal;
import emavalidator.columns.ReleaseHistoryPhysicalHV;
import emavalidator.columns.Start;
import emavalidator.errors.RowErrorQuestionableStart;


public class RowValidatorQuestionableStart extends AbstractRowValidator
{
    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try {
            // start, release history original, release history physical hv
            String startDate = rowValues.get(Start.class.getSimpleName());
            String theatricalReleaseDate = rowValues.get(ReleaseHistoryOriginal.class.getSimpleName());
            String physicalReleaseDate = rowValues.get(ReleaseHistoryPhysicalHV.class.getSimpleName());
            String licenseType = rowValues.get(LicenseType.class.getSimpleName());
            
            if(licenseType.compareToIgnoreCase(ValidatorUtils.LICENSE_TYPE_PRE_ORDER_EST) == 0)
                return true;
    
            if( ValidatorUtils.isQuestionableStartDate(startDate, theatricalReleaseDate) ) {
                // Error
                ErrorLog.appendError(new RowErrorQuestionableStart(
                                     rowNumber,
                                     RowErrorQuestionableStart.START_LESS_THEATRICAL_ERROR,
                                     ErrorLevel.WARNING,
                                     "Start: " + startDate + " ReleaseHistoryOriginal: " + theatricalReleaseDate,
                                     RowErrorQuestionableStart.EXPECTED_VALUE_THEATRICAL));
            }
    
            if( ValidatorUtils.isQuestionableStartDate(startDate, physicalReleaseDate) ) {
                // Error
                ErrorLog.appendError(new RowErrorQuestionableStart(
                                     rowNumber,
                                     RowErrorQuestionableStart.START_LESS_PHYSICAL_ERROR,
                                     ErrorLevel.WARNING,
                                     "Start: " + startDate + " ReleaseHistoryPhysicalHV: " + physicalReleaseDate,
                                     RowErrorQuestionableStart.EXPECTED_VALUE_PHYSICAL));
            }
            return false;
        } catch(NullPointerException NPE) { return true; } // do not validate when column values are missing

    }
}
