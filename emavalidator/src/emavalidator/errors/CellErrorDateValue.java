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

public class CellErrorDateValue extends AbstractErrorEntry
{
    public static final String YEAR_FORMAT_ERROR = "Year Value Error: The value for this cell must be a specific year format.";
    public static final String DATE_FORMAT_ERROR = "Date Value Error: The value for this cell must be one of two date formats.";
    public static final String DATE_START_END_ERROR = "Date Value Error: The start date must be before the end date.";
    public static final String DATE_VALIDATION_ERROR = "Date Value Error: The given date is invalid.";
    public static final String START_DATE_EMPTY_ERROR = "Date Value Error: The start date must not be empty.";
    public static final String END_DATE_EMPTY_ERROR = "Date Value Error: The end date must not be empty.";
    
    public static final String EXPECTED_VALUES        = "Dates must be in (1) YYYY-MM-DD or (2) ISO-8601 Date+Time format.";
    public static final String EXPECTED_VALUES_YEAR = "YYYY values such as 2014, 2039, 1825, etc.";

    public CellErrorDateValue(int rowNumber, int columnNumber, String message, String value, String expected)
    {
        super(rowNumber, columnNumber, message, ErrorLevel.CRITICAL, value, expected);
    }
}
