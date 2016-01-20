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

import emavalidator.ErrorLog;
import emavalidator.AbstractRowValidator;
import emavalidator.columns.EpisodeNumber;
import emavalidator.columns.WorkType;
import emavalidator.errors.RowErrorEpisodeNumber;

public class RowValidatorEpisodeNumber extends AbstractRowValidator
{
    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String workType = rowValues.get(WorkType.class.getSimpleName());
            String episodeNumber = rowValues.get(EpisodeNumber.class.getSimpleName());

            if(workType == null || episodeNumber == null)
                return true;

            if(workType.toLowerCase().compareToIgnoreCase("episode") == 0) // if it's an episode
            {
                if(!episodeNumber.matches(ValidatorUtils.NUMBER_FORMAT_REGEX)) // and it's not an episode number
                    ErrorLog.appendError(new RowErrorEpisodeNumber(rowNumber, "WorkType: " + workType + " " + "EpisodeNumber: " + episodeNumber));

                return false;
            }
            return true;
        }
        catch (NullPointerException NPE) { return true; } // do not attempt to validate when values are missing from the row
    }
}
