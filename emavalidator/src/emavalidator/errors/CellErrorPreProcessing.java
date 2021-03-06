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

public class CellErrorPreProcessing extends AbstractErrorEntry
{
    public static final String INVALID_CELL_CHARACTER_TAB = "Invalid character in cell: Tab (\\t)";
    public static final String INVALID_CELL_CHARACTER_NEWLINE = "Invalid character in cell: New Line (\\n)";
    public static final String INVALID_CELL_CHARACTER_CARRIAGERETURN = "Invalid character in cell: Carriage Return (\\r)";
    public static final String INVALID_CELL_CHARACTER_WHITESPACE = "Extra leading and/or trailing whitespace in cell";

    public CellErrorPreProcessing(int rowNumber, int columnNumber, String message, String value, String expected)
    {
        super(rowNumber, columnNumber, message, ErrorLevel.WARNING, value, expected);
    }
}
