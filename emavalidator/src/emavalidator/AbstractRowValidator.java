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

import java.util.HashMap;

/**
 * A RowValidator should be used in scenarios where different column values and their 'correctness' are based off of values in other columns.
 * These different cell values are indexed via their class name to be accessed at row validation time.
 * This requires that every value encountered in the sheet be appended to the map during execution so each RowValidator can access the values it needs.
 * @author canavan
 */
public abstract class AbstractRowValidator
{
    public abstract boolean validate(HashMap<String, String> rowValues, int rowNumber);
}
