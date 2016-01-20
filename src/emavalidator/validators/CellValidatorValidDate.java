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

import emavalidator.ErrorLog;
import emavalidator.AbstractCellValidator;
import emavalidator.errors.CellErrorDateValue;

public class CellValidatorValidDate extends AbstractCellValidator
{
    private boolean allowsEmptyValues;
    private String dateClassName = "";

    public CellValidatorValidDate(boolean allowsEmptyValues, String dateClassName) { this.allowsEmptyValues = allowsEmptyValues; this.dateClassName = dateClassName; }

    @Override
    public boolean validate(String inputString, int rowNumber, int columnNumber)
    {
        if(allowsEmptyValues)
            if(inputString.matches(ValidatorUtils.EMPTY_STRING_REGEX))
                return true;
        
        // Allow "N/A" for SuppressionLiftDate
        if(this.dateClassName.equals("SuppressionLiftDate"))
        {
            if(inputString.equalsIgnoreCase("n/a"))
            {
                return true;
            }
        }

        if(ValidatorUtils.isValidStartEndDate(this.dateClassName, inputString))
            return true;

        ErrorLog.appendError(new CellErrorDateValue(rowNumber, columnNumber, CellErrorDateValue.DATE_FORMAT_ERROR, inputString, CellErrorDateValue.EXPECTED_VALUES));
        return false;
    }
}
