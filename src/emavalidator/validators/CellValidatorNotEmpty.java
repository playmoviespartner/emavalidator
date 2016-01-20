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
import emavalidator.errors.CellErrorEmptyValue;

public class CellValidatorNotEmpty extends AbstractCellValidator
{
    public static final String NON_BLANK_VALUES_EXPECTED = "A non blank value is expected here";
    @Override
    public boolean validate(String inputString, int rowNumber, int columnNumber)
    {
        if(!inputString.matches(ValidatorUtils.EMPTY_STRING_REGEX))
            return true;

        ErrorLog.appendError(new CellErrorEmptyValue(rowNumber, columnNumber, CellErrorEmptyValue.VALUE_IS_REQUIRED, inputString, CellValidatorNotEmpty.NON_BLANK_VALUES_EXPECTED));
        return false;
    }
}
