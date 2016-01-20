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

public class CellErrorHeaderProcessing extends AbstractErrorEntry
{
    public static final String MISMATCHING_HEADER_ROW_LENGTH_LONG  = "More columns found than expected.";
    public static final String MISMATCHING_HEADER_ROW_LENGTH_SHORT = "Fewer columns found than expected.";
    public static final String MISMATCHING_HEADER_CONTENTS         = "Invalid column header contents.";

    public CellErrorHeaderProcessing(int rowNumber, int columnNumber, String message, String value, String expected)
    {
        super(rowNumber, columnNumber, message, ErrorLevel.CRITICAL, value, expected);
    }
}
