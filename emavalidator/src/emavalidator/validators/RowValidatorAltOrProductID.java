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

import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.AltID;
import emavalidator.columns.ProductID;
import emavalidator.errors.RowErrorEIDRValueCheck;

/* 
 * This RowValidator only used in EMASpec 1.5 and 1.6
 */
public class RowValidatorAltOrProductID extends AbstractRowValidator
{

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {

        try {
            String productId = rowValues.get(ProductID.class.getSimpleName());
            String altId = rowValues.get(AltID.class.getSimpleName());
            
            if (productId.isEmpty() && altId.isEmpty()) {
                ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, 
                                                                RowErrorEIDRValueCheck.ERROR_NO_EIDR_OR_ID,
                                                                "ProductID and AltID are empty.",
                                                                RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
            }
        
        } catch (NullPointerException NPE) {
            return true;
        }
        return true;
    }

}
