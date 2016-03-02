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

public class CellErrorMissingColumn extends AbstractErrorEntry
{
    public static final String CELL_ERROR_MISSING_COLUMN = "Missing column.";
    public static final String EXPECTED_VALUES_MISSING_COLUMN = "All column headers (required and optional) for an EMASpec must be present in the EMA Avail.";

    public CellErrorMissingColumn(String value)
    {
        super(CellErrorMissingColumn.CELL_ERROR_MISSING_COLUMN, ErrorLevel.CRITICAL, value, CellErrorMissingColumn.EXPECTED_VALUES_MISSING_COLUMN);
    }
}
