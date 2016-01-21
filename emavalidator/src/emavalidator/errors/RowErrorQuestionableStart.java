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

public class RowErrorQuestionableStart extends AbstractErrorEntry
{
    public static final String START_LESS_THEATRICAL_ERROR = "Start date is earlier than ReleaseHistoryOriginal.";
    public static final String START_LESS_PHYSICAL_ERROR = "Start date is earlier than ReleaseHistoryPhysicalHV.";

    
    public static final String EXPECTED_VALUE_THEATRICAL = "In most cases the theatrical release date is before the start date.";
    public static final String EXPECTED_VALUE_PHYSICAL = "In most cases the physical release date is before the start date.";
    
    public RowErrorQuestionableStart(int rowNumber, String errorMessage, ErrorLevel errorLevel, String actualValue, String expectedValue)
    {
        super(rowNumber, errorMessage, errorLevel, actualValue, expectedValue);
        // TODO Auto-generated constructor stub
    }

}
