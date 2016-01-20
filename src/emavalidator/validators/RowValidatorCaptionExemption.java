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

import emavalidator.AbstractEMASpec;
import emavalidator.ErrorLog;
import emavalidator.AbstractRowValidator;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.columns.CaptionExemption;
import emavalidator.columns.CaptionIncluded;
import emavalidator.columns.Territory;
import emavalidator.columns.WorkType;
import emavalidator.errors.RowErrorCaptions;

public class RowValidatorCaptionExemption extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;

    @SuppressWarnings("unused")
    private RowValidatorCaptionExemption() {}

    public RowValidatorCaptionExemption(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String countryCode = rowValues.get(Territory.class.getSimpleName());
            String captionIncluded = rowValues.get(CaptionIncluded.class.getSimpleName());
            String captionExemption = rowValues.get(CaptionExemption.class.getSimpleName());

            if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV) // if we're validating a TV Spec
            {
                String workType = rowValues.get(WorkType.class.getSimpleName()); // if the current value is a Season identifier, no caption information is required
                if(workType.contains("season"))
                    return true;
            }

            if(countryCode.matches(ValidatorUtils.UNITED_STATES_COUNTRY_CODE_VALUE_REGEX)) // if U.S. line item
                if(captionIncluded.matches(ValidatorUtils.NO_REGEX)) // if captions are not included
                    if(!captionExemption.matches(ValidatorUtils.CAPTION_EXEMPTION_VALUES_REGEX))
                        ErrorLog.appendError(new RowErrorCaptions(rowNumber, RowErrorCaptions.CAPTION_EXEMPTION_ERROR, ErrorLevel.ERROR, captionExemption, RowErrorCaptions.CAPTION_EXEMPTION_EXPECTED));
            return false;
        }
        catch (NullPointerException NPE) { return true; } // do not need to throw an error as missing country will already
    }
}
