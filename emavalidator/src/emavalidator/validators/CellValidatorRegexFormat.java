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

import org.apache.commons.lang3.StringEscapeUtils;

import emavalidator.AbstractCellValidator;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.ErrorLog;
import emavalidator.errors.CellErrorSpecificValueFormat;

public class CellValidatorRegexFormat extends AbstractCellValidator
{
    private String[] regexPatterns;
    private String errorMessage;
    boolean matchAll;
    private String expectedValues;
    private ErrorLevel errorLevel;

    /**
     * A regex validator that can support multiple regexes
     * @param regexPatterns The pattern or patterns to match against during validation
     * @param matchAll Whether to match against all the patterns input or just a single one
     * @param errorLevel WARNING or CRITICAL error level.
     * @param errorMessage The error message to add to the ErrorLog when matching fails
     * @param expectedValues The list of acceptable values to print back to the user when validation fails
     */
    public CellValidatorRegexFormat(String[] regexPatterns, boolean matchAll, ErrorLevel errorLevel, String errorMessage, String expectedValues)
    {
        this.regexPatterns = regexPatterns;
        this.errorMessage = errorMessage;
        this.matchAll = matchAll;
        this.expectedValues = expectedValues;
        this.errorLevel = errorLevel;
    }

    @SuppressWarnings("unused")
    private CellValidatorRegexFormat() { }

    @Override
    public boolean validate(String inputString, int rowNumber, int columnNumber)
    {
        if(matchAll && regexPatterns.length > 1)
        {
            for(String currentPattern : regexPatterns)
                if(!inputString.matches(currentPattern))
                {
                    ErrorLog.appendError(new CellErrorSpecificValueFormat(rowNumber, columnNumber, this.errorMessage, this.errorLevel, StringEscapeUtils.escapeJava(inputString), expectedValues));
                    return false;
                }
            return true;
        }
        else
        {
            for(String currentPattern : regexPatterns)
                if(inputString.matches(currentPattern))
                    return true;
            ErrorLog.appendError(new CellErrorSpecificValueFormat(rowNumber, columnNumber, this.errorMessage, this.errorLevel, StringEscapeUtils.escapeJava(inputString), expectedValues));
            return false;
        }
    }
}
