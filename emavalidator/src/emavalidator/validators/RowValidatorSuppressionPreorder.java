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

import emavalidator.ErrorLog;
import emavalidator.AbstractRowValidator;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.columns.LicenseRightsDescription;
import emavalidator.columns.SuppressionLiftDate;
import emavalidator.errors.RowErrorSuppressionPreorder;

public class RowValidatorSuppressionPreorder extends AbstractRowValidator
{
    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String licenseRightsDescription = rowValues.get(LicenseRightsDescription.class.getSimpleName());
            String suppressionLiftDate = rowValues.get(SuppressionLiftDate.class.getSimpleName());

            if(licenseRightsDescription.contains("preorder"))
                if(!suppressionLiftDate.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX) &&
                   !suppressionLiftDate.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX))
                {
                        ErrorLog.appendError(new RowErrorSuppressionPreorder(
                                             rowNumber,
                                             RowErrorSuppressionPreorder.SUPPRESSION_PREORDER_ERROR,
                                             ErrorLevel.ERROR,
                                             licenseRightsDescription + " : " + suppressionLiftDate,
                                             RowErrorSuppressionPreorder.SUPPRESSION_PREORDER_EXPECTED));
                        return false;
                }
            return true;
        }
        catch (NullPointerException NPE) { return true; }
    }
}
