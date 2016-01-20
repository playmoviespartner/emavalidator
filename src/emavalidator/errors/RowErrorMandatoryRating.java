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

public class RowErrorMandatoryRating extends AbstractErrorEntry
{
    public static final String MANDATORY_RATING_ERROR = "For this specific country, rating values are not optional. Please add rating value and rating system.";
    public static final String EXPECTED_VALUES_RATING = "E.g. RatingSystem: MPAA    RatingValue: PG";

	public RowErrorMandatoryRating(int rowNumber, String value)
	{
		super(rowNumber, RowErrorMandatoryRating.MANDATORY_RATING_ERROR, ErrorLevel.WARNING, value, RowErrorMandatoryRating.EXPECTED_VALUES_RATING);
	}

}
