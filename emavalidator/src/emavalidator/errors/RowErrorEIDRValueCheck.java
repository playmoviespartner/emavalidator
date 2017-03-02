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

public class RowErrorEIDRValueCheck extends AbstractErrorEntry
{
    public static final String ERROR_NO_EIDR_OR_ID = "A unique asset cannot be identified without either its unique EIDR-2 value or a generated unique ID.";
    public static final String EXPECTED_EIDR_OR_ID = "Please provide the asset's EIDR-2 value (e.g. 10.5240/1489-49A2-3956-4B2D-ZZ44-7) or generate a unique ID for it (e.g. X5123D8G).";

    public static final String ERROR_NO_EIDR = "A unique asset cannot be identified without its unique EIDR value.";
    public static final String EXPECTED_EIDR = "Please provide the asset's EIDR value (e.g. urn:eidr:10.5240:1489-49A2-3956-4B2D-ZZ44-7).";

    public static final String ERROR_CANNOT_BE_BLANK = "This cell cannot be blank for the specified WorkType.";
    public static final String ERROR_EPISODE_CONTENT_ID_CANNOT_HAVE_VALUE = "EpisodeContentID cannot contain a value for the specified WorkType.";
    public static final String ERROR_EPISODE_ID_CANNOT_HAVE_VALUE = "EpisodeID cannot contain a value for the specified WorkType.";

    public static final String EXPECTED_BLANK = "";
    
    public RowErrorEIDRValueCheck(int rowNumber, String errorMessage, String actualValue, String expectedValue)
    {
        super(rowNumber, errorMessage, ErrorLevel.ERROR, actualValue, expectedValue);
    }
}
