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
import emavalidator.errors.CellErrorSpecialSymbols;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class CellValidatorSpecialSymbols extends AbstractCellValidator
{
    private String illegalCharacterList;
    private String errorMessage;
    private String expected;

    public CellValidatorSpecialSymbols(String illegalCharacterList, String errorMessage, String expected)
    {
        this.illegalCharacterList = illegalCharacterList;
        this.errorMessage = errorMessage;
        this.expected = expected;
    }

    @Override
    public boolean validate(String inputString, int rowNumber, int columnNumber)
    {
        boolean value = StringUtils.containsAny(inputString, this.illegalCharacterList);
        if(!value)
            return true;
        ErrorLog.appendError(new CellErrorSpecialSymbols(rowNumber, columnNumber, errorMessage, StringEscapeUtils.escapeJava(inputString), this.expected));
        return false;
    }
}
