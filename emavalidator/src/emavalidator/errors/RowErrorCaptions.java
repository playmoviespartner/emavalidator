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

public class RowErrorCaptions extends AbstractErrorEntry
{
    public static final String CAPTION_INCLUDED_ERROR = "CaptionIncluded must be 'Yes' or 'No' for all U.S. line items.";
    public static final String CAPTION_INCLUDED_EXPECTED = "'Yes' or 'No'. Optional for all territories except the U.S.";

    public static final String CAPTION_REQUIRED_ERROR = "CaptionRequired must be 'Yes' or 'No' for all U.S. line items.";
    public static final String CAPTION_REQUIRED_EXPECTED = "'Yes' or 'No'. Optional for all territories except the U.S.";

    public static final String CAPTION_EXEMPTION_ERROR = "CaptionExemption must be included for the U.S. when captions are not included.";
    public static final String CAPTION_EXEMPTION_EXPECTED = "The following values are valid for CaptionExemption: 1, 2, 3, 4, 5, 6";

    public RowErrorCaptions(int rowNumber, String errorMessage, ErrorLevel errorLevel, String value, String expected)
    {
        super(rowNumber, errorMessage, errorLevel, value, expected);
    }
}
