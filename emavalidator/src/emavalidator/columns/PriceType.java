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

package emavalidator.columns;

import emavalidator.AbstractColumnDefinition;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.errors.CellErrorSpecificValueFormat;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class PriceType extends AbstractColumnDefinition
{
    public static enum PriceTypeValues
    {
        Tier, WSP, SRP, Category;

        /**
         * @return this.toString().toLowerCase()
         */
        public String toLowerString()
        {
            return this.toString().toLowerCase();
        }

        public static String getValueNames()
        {
            StringBuilder result = new StringBuilder();
            for(PriceTypeValues currentValue : PriceTypeValues.values())
                result.append(currentValue.toString() + ", ");
            if(result.length() >= 3)
                result.setLength(result.length() -2); // chop off the extra ", " from the end
            return result.toString();
        }
    }

    @Override
    public void buildValidators()
    {
        this.validators.add(new CellValidatorRegexFormat(new String[] {
                            ValidatorUtils.PRICE_TYPE_VALUES_REGEX,
                            ValidatorUtils.EMPTY_STRING_REGEX}, // this field is required but this error will be caught in the row validator to prevent duplicate errors
                            false,
                            ErrorLevel.ERROR,
                            CellErrorSpecificValueFormat.SPECIFIC_VALUES_ONLY_ERROR,
                            PriceType.PriceTypeValues.getValueNames()));
    }
}
