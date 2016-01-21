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

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import emavalidator.ErrorLog;
import emavalidator.AbstractRowValidator;
import emavalidator.columns.Any;
import emavalidator.columns.Description;
import emavalidator.columns.ExceptionFlag;
import emavalidator.columns.Metadata;
import emavalidator.columns.OtherInstructions;
import emavalidator.columns.OtherTerms;
import emavalidator.errors.RowErrorExceptionFlagSet;

public class RowValidatorExceptionFlagSet extends AbstractRowValidator
{
    private ArrayList<Class<?>> freeformClasses = new ArrayList<Class<?>>();

    public RowValidatorExceptionFlagSet()
    {
        super();
        freeformClasses.add(Description.class);       // 1.6, 1.5, 1.4
        freeformClasses.add(OtherTerms.class);        // 1.6, 1.5, 1.4
        freeformClasses.add(OtherInstructions.class); // 1.6, 1.5, 1.4
        freeformClasses.add(Metadata.class);          // 1.6, 1.5, 1.4
        freeformClasses.add(Any.class);               // 1.6, 1.5, 1.4
    }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            boolean foundContent = false;

            for(Class<?> currentClass : this.freeformClasses)
                    if(StringUtils.isNotBlank(rowValues.get(currentClass.getSimpleName()).toString()))
                        foundContent =  true;

            if(foundContent)
            {
                if(!rowValues.get(ExceptionFlag.class.getSimpleName()).matches(ValidatorUtils.YES_REGEX))
                {
                    ErrorLog.appendError(new RowErrorExceptionFlagSet(
                                         rowNumber,
                                         RowErrorExceptionFlagSet.EXCEPTION_FLAG_NOT_SET_ERROR,
                                         rowValues.get(ExceptionFlag.class.getSimpleName()),
                                         "Yes"));
                    return false;
                }
            }
        }
        catch (NullPointerException npe) { }
        return true;
    }
}
