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
import emavalidator.errors.CellErrorSpecialSymbols;
import emavalidator.errors.CellErrorSpecificValueFormat;
import emavalidator.validators.CellValidatorNotEmpty;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.CellValidatorSpecialSymbols;
import emavalidator.validators.ValidatorUtils;

public class AltID extends AbstractColumnDefinition
{
    /**
     * @param required Whether the field is required or not
     */
    public AltID(boolean required) { super(required); }

    @Override
    public void buildValidators()
    {

        if(required)
        {
            String valuesRegex = ValidatorUtils.VALID_ALTID_REGEX;
            this.validators.add(new CellValidatorSpecialSymbols(
                                ValidatorUtils.ILLEGAL_METADATA_CHARACTERS,
                                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS,
                                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS_EXPECTED));
            this.validators.add(new CellValidatorRegexFormat(new String[]{
                                valuesRegex },
                                false,
                                ErrorLevel.ERROR,
                                CellErrorSpecificValueFormat.ALT_ID_FORMAT_ERROR,
                                CellErrorSpecificValueFormat.EXPECTED_VALUES_ALT_ID));
            //this.validators.add(new CellValidatorNotEmpty());
        }
        else
        {
            this.validators.add(new CellValidatorSpecialSymbols(
                                ValidatorUtils.ILLEGAL_METADATA_CHARACTERS,
                                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS,
                                CellErrorSpecialSymbols.ILLEGAL_METADATA_CHARACTERS_EXPECTED));
        }
    }
}
