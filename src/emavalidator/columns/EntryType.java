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
import emavalidator.AbstractEMASpec;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.errors.CellErrorEntryType;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class EntryType extends AbstractColumnDefinition
{
    private boolean required;

    public static final String FULL_EXTRACT = "Full Extract";
    public static final String FULL_DELETE = "Full Delete";
        
    /**
     * @param emaVersion The EMAVersion that this column originated from.
     * @param required Whether the field is required or not.
     */
    public EntryType(AbstractEMASpec.EMAVersion emaVersion, boolean required)
    {
        super(emaVersion, required);
    }

    @Override
    public void buildValidators()
    {
        String valuesRegex = "";
        String valuesAccepted = "";

        if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16 ||
           this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV ||
           this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec15)
        {
            valuesRegex = ValidatorUtils.ENTRY_TYPE_VALUES_REGEX_15_16;
            valuesAccepted = CellErrorEntryType.VALUES_ACCEPTED_15_16;
        }
        else if(this.emaVersion == (AbstractEMASpec.EMAVersion.EMASpec14))
        {
            valuesRegex = ValidatorUtils.ENTRY_TYPE_VALUES_REGEX_14;
            valuesAccepted = CellErrorEntryType.VALUES_ACCEPTED_14;
        }
        else
            throw new IllegalArgumentException("Can't instantiate unknown EMA spec version for Disposition Entry Type");

        if(required)
        {
            this.validators.add(new CellValidatorRegexFormat(new String[]{
                                valuesRegex},
                                false,
                                ErrorLevel.ERROR,
                                CellErrorEntryType.VERSION_SPECIFIC_ERROR,
                                valuesAccepted));
        }
        else
        {
            this.validators.add(new CellValidatorRegexFormat(new String[]{
                                valuesRegex,
                                ValidatorUtils.EMPTY_STRING_REGEX},
                                false,
                                ErrorLevel.ERROR,
                                CellErrorEntryType.VERSION_SPECIFIC_ERROR,
                                valuesAccepted));
        }
    }
}
