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

public class CellErrorSpecialSymbols extends AbstractErrorEntry
{
    //Is metadata character the correct term here?
    public static final String ILLEGAL_METADATA_CHARACTERS = "Invalid character in cell: No metadata characters allowed.";
    public static final String ILLEGAL_SPECIAL_CHARACTERS = "Invalid character in cell: No special characters allowed.";

    public static final String ILLEGAL_METADATA_CHARACTERS_EXPECTED = "Please remove all of the following: \\r, \\n, \\t";
    public static final String ILLEGAL_SPECIAL_CHARACTERS_EXPECTED = "Please remove all special characters (e.g. !@#$%^&).";

    public CellErrorSpecialSymbols(int rowNumber, int columnNumber, String message, String value, String expected)
    {
        super(rowNumber, columnNumber, message, ErrorLevel.ERROR, value, expected);
    }
}
