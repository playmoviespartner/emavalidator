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

package emavalidator.validators;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import emavalidator.ErrorLog;
import emavalidator.AbstractRowValidator;
import emavalidator.columns.RatingSystem;
import emavalidator.columns.RatingValue;
import emavalidator.columns.Territory;
import emavalidator.errors.RowErrorMandatoryRating;

public class RowValidatorMandatoryRating extends AbstractRowValidator
{
	/**
	*	Australia
	*	New Zealand
	*	France
	*	Germany
	*	Italy
	*	Spain
	*   Korea
	*   Russia
	*   Brazil
	 */
	public static final String MANDATORY_COUNTRIES_REGEX = "(?i)^(AU|NZ|FR|DE|IT|ES|KR|RU|BR)$";

	@Override
	public boolean validate(HashMap<String, String> rowValues, int rowNumber)
	{
	    try
	    {
    		String country = rowValues.get(Territory.class.getSimpleName());
    		if(country.matches(RowValidatorMandatoryRating.MANDATORY_COUNTRIES_REGEX))
    		{
    			String ratingValue = rowValues.get(RatingValue.class.getSimpleName());
    			String ratingSystem = rowValues.get(RatingSystem.class.getSimpleName());

    			if(StringUtils.isEmpty(ratingValue))
    			{
    				StringBuilder returnValue = new StringBuilder();
    				returnValue.append("Country: '" + country + "' ");
    				returnValue.append("RatingSystem: '" + ratingSystem + "' ");
    				returnValue.append("RatingValue: '" + ratingValue + "'");
    				ErrorLog.appendError(new RowErrorMandatoryRating(rowNumber, returnValue.toString()));
    				return false;
    			}
    		}
	    }
	    catch (NullPointerException NPE) {}
		return true;
	}
}
