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

package emavalidator;

/**
 * Implementations of CellValidator take a cell's contents in as a string and validate it against their own unique criteria.
 * The row and column number are needed in order to generate the appropriate error that the validator generate when validating.
 * A boolean return value is used on the validate call in order to return the validation status to the caller.
 * If an error is encountered, the validator should make its own manual call to the ErrorLog and append a new entry.
 * @author canavan
 */
public abstract class AbstractCellValidator
{
    /**
     * @param inputValue The value encountered from the input source during validation time. This is what gets 'validated'.
     * @param rowNumber The row number where this value occurred. Used to report the row number to the error log when a validation error occurs.
     * @param columnNumber The column number where this value occurred. Used to report the column number to the error log when a validation error occurs.
     * @return True if the value successfully validated and there were no errors added to the ErrorLog, false otherwise
     */
    public abstract boolean validate(String inputValue, int rowNumber, int columnNumber);
}
