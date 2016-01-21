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
import emavalidator.errors.CellErrorWorkType;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class WorkType extends AbstractColumnDefinition
{
    @Override
    public void buildValidators()
    {
        this.validators.add(new CellValidatorRegexFormat(new String[] {
                            ValidatorUtils.WORK_TYPE_VALUES_REGEX},
                            false,
                            ErrorLevel.ERROR,
                            CellErrorSpecificValueFormat.SPECIFIC_VALUES_ONLY_ERROR,
                            CellErrorWorkType.EXPECTED_WORK_TYPE));
    }

    public static boolean isEpisode(String inputWorkTypeValue)
    {
        return inputWorkTypeValue.toLowerCase().contains("episode");
    }

    public static boolean isSeason(String inputWorkTypeValue)
    {
        return inputWorkTypeValue.toLowerCase().contains("season");
    }

    public static boolean isMovie(String inputWorkTypeValue)
    {
        return inputWorkTypeValue.toLowerCase().contains("movie") ||
               inputWorkTypeValue.toLowerCase().contains("short");
    }
    
    public static boolean isCollection(String inputWorkTypeValue)
    {
        return inputWorkTypeValue.toLowerCase().contains("collection");
    }
    
    public static boolean isSeries(String inputWorkTypeValue)
    {
        return inputWorkTypeValue.toLowerCase().contains("series");
    }
}
