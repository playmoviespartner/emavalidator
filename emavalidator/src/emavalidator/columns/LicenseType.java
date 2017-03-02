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
import emavalidator.errors.CellErrorLicenseType;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class LicenseType extends AbstractColumnDefinition
{

    public LicenseType(AbstractEMASpec.EMAVersion emaVersion) { super(emaVersion); }

    @Override
    public void buildValidators()
    {
        String valuesRegex = "";
        String valuesAccepted = "";

        if( this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17 ||
            this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17TV ||
            this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16 ||
            this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV)
        {
            valuesRegex = ValidatorUtils.LICENSE_TYPE_VALUES_REGEX_16;
            valuesAccepted = CellErrorLicenseType.VALUES_ACCEPTED_16;
        }
        else if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec14 ||
                this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec15)
        {
            valuesRegex = ValidatorUtils.LICENSE_TYPE_VALUES_REGEX_14_15;
            valuesAccepted = CellErrorLicenseType.VALUES_ACCEPTED_14_15;
        }
        else
            throw new IllegalArgumentException("Can't instantiate unknown EMA spec version for License Type");

        this.validators.add(new CellValidatorRegexFormat(new String[] {
                            valuesRegex,
                            ValidatorUtils.EMPTY_STRING_REGEX}, // This field is required but will be caught in the row validator.
                            false,
                            ErrorLevel.ERROR,
                            CellErrorLicenseType.VERSION_SPECIFIC_ERROR,
                            valuesAccepted));
    }
}
