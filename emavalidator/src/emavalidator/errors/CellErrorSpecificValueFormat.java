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

package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class CellErrorSpecificValueFormat extends AbstractErrorEntry
{
    public static final String DATE_FORMAT_ERROR                 = "Date value error: The value for this cell must be an accepted date format.";
    public static final String EIDR_FORMAT_ERROR                 = "EIDR format error: The value for this cell must be a valid EIDR format.";
    // This should be freeform(ish)
    public static final String AVAIL_ID_FORMAT_ERROR             = "AvailID format error: The value for this cell must be an accepted AVAIL ID format.";
    // This check is wrong
    public static final String ISO_TWO_DIGIT_LANGUAGE_CODE_ERROR = "ISO code format error: The value for this cell must be a valid BCP-47 language code value.";
    public static final String ISO_TWO_DIGIT_COUNTRY_CODE_ERROR  = "ISO code format error: The value for this cell must be a valid 2 digit ISO 3166-1 alpha-2 code value.";
    public static final String NUMBER_LETTER_FORMAT_ERROR        = "Invalid character(s) in cell: The value for this cell can contain only numbers and letters.";
    public static final String YES_OR_NO_ONLY_ERROR              = "Invalid cell value: The value for this cell must be either 'Yes' or 'No'.";
    public static final String SPECIFIC_VALUES_ONLY_ERROR        = "Invalid cell value: The values for this cell are predefined and concrete.";
    public static final String NUMBER_FORMAT_ERROR               = "Invalid cell value: The value for this cell must be an exact number value.";
    public static final String FLOAT_FORMAT_ERROR                = "Invalid cell value: The value for this cell must be a floating point number value.";
    public static final String DECIMAL_FORMAT_ERROR              = "Invalid cell value: The value for this cell must be a decimal number value.";
    public static final String COMMA_SEPARATED_VALUES_ERROR      = "Invalid cell value: The value for this cell must be a comma separated list of values.";
    // This check is too strict
    public static final String ALT_ID_FORMAT_ERROR               = "Invalid cell value: The value for this cell may only contain alphanumeric characters (and some special characters).";

    public static final String EXPECTED_VALUES_NUMBER  = "This column expects whole numbers only (e.g. 1, 24, 1410). Optional columns may also be left blank.";
    public static final String EXPECTED_VALUES_FLOAT   = "This column expects float numbers only (e.g. .23, 0.35, 3.21). Optional columns may also be left blank.";
    public static final String EXPECTED_VALUES_DECIMAL = "This column expects decimal numbers only (e.g. .03, 0.43, .38932). Optional columns may also be left blank.";
    public static final String EXPECTED_VALUES_SUBDUB  = "This column expects sub / dub values only (e.g. sub, dub, subdub, any). Optional columns may also be left blank.";
    public static final String EXPECTED_VALUES_YESNO   = "This column expects yes / no values only (e.g. Yes, yes, YES, No, no, NO).";
    public static final String EXPECTED_VALUES_ALT_ID  = "This column expects alphanumeric characters, hyphens (-), underscores (_), periods (.), arrobas (@), or forward slashes (/).";

    public CellErrorSpecificValueFormat(int rowNumber, int columnNumber, String message, ErrorLevel errorLevel, String value, String expected)
    {
        super(rowNumber, columnNumber, message, errorLevel, value, expected);
    }
}
