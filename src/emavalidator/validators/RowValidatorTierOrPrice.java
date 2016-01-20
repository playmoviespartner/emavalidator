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
import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.EntryType;
import emavalidator.columns.PriceType;
import emavalidator.columns.PriceValue;
import emavalidator.columns.SRP;
import emavalidator.columns.Tier;
import emavalidator.columns.WSP;
import emavalidator.errors.RowErrorTierOrPrice;

public class RowValidatorTierOrPrice extends AbstractRowValidator
{
    public static final String CATEGORY_EXPECTED_VALUES = "New Release, Library, Mega-Library, DD-Theatrical, Pre-Theatrical, Early EST, Preorder EST, Early VOD, Preorder VOD";
    public static final String PRICE_TYPE_PRICE_VALUE = "PriceType and PriceValue go hand in hand. Please be sure their values correspond correctly from the template";

    private AbstractEMASpec.EMAVersion emaVersion;

    public RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        //TODO(canavan) make this prettier
        if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec14)
        {
            try
            {
                String wholesalePrice = rowValues.get(WSP.class.getSimpleName());
                String suggestedPrice = rowValues.get(SRP.class.getSimpleName());
                String tier           = rowValues.get(Tier.class.getSimpleName());

                if(wholesalePrice.isEmpty() && suggestedPrice.isEmpty() && tier.isEmpty())
                {
                    ErrorLog.appendError(new RowErrorTierOrPrice(
                                         rowNumber,
                                         RowErrorTierOrPrice.TIER_OR_PRICE,
                                         "WSP: " + wholesalePrice + " SRP: " + suggestedPrice + " Tier: " + tier, ""));
                    return false;
                }
                if((!wholesalePrice.isEmpty()) && (!suggestedPrice.isEmpty()))
                {
                    ErrorLog.appendError(new RowErrorTierOrPrice(
                                         rowNumber,
                                         RowErrorTierOrPrice.WSP_AND_SRP,
                                         "WSP: " + wholesalePrice + " SRP: " + suggestedPrice + " Tier: " + tier, ""));
                    return false;
                }
                if(tier.length() != 0 && (wholesalePrice.length() != 0 || suggestedPrice.length() != 0))
                {
                    ErrorLog.appendError(new RowErrorTierOrPrice(
                                         rowNumber,
                                         RowErrorTierOrPrice.TIER_AND_PRICE,
                                         "WSP: " + wholesalePrice + " SRP: " + suggestedPrice + " Tier: " + tier, ""));
                    return false;
                }
                return true;
            }
            catch (NullPointerException NPE) { return true; } // do not validate against missing values. column structure cannot be guaranteed therefore these values cannot be guaranteed to exist

        }
        else if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec15 ||
                this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16 ||
                this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV ||
                this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17TV)
        {
            try
            {
                String entryType  = rowValues.get( EntryType.class.getSimpleName());
                String priceType  = rowValues.get( PriceType.class.getSimpleName()).toLowerCase();
                String priceValue = rowValues.get(PriceValue.class.getSimpleName()).toLowerCase();
                
                // Check for empty cells first.
                if(priceType.isEmpty())
                {
                    // Ignore PriceType if EntryType is "Full Delete"
                    if(entryType.equals(EntryType.FULL_DELETE)) {
                        
                    } else {
                        ErrorLog.appendError(new RowErrorTierOrPrice(
                                rowNumber,
                                RowErrorTierOrPrice.EMPTY_PRICE_TYPE,
                                priceType,
                                RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
                        return false;
                    }
                }
                if(priceValue.isEmpty())
                {
                    if(entryType.equals(EntryType.FULL_DELETE)) {
                        
                    } else {
                        ErrorLog.appendError(new RowErrorTierOrPrice(
                                rowNumber,
                                RowErrorTierOrPrice.EMPTY_PRICE_VALUE,
                                priceValue,
                                RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
                        return false;
                    }
                }

                // Cells are non-empty, now check their contents.
                if(priceType.compareTo(PriceType.PriceTypeValues.Category.toLowerString()) == 0)
                {
                    if(!priceValue.matches(ValidatorUtils.VALID_TIER_CATEGORY_VALUES_REGEX))
                    {
                        ErrorLog.appendError(new RowErrorTierOrPrice(
                                             rowNumber,
                                             RowErrorTierOrPrice.CATEGORY_VALUE,
                                             priceValue,
                                             RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
                        return false;
                    }
                }
                else if(priceType.compareTo(PriceType.PriceTypeValues.SRP.toLowerString()) == 0 ||
                        priceType.compareTo(PriceType.PriceTypeValues.WSP.toLowerString()) == 0)
                {
                    if(!priceValue.matches(ValidatorUtils.FLOAT_FORMAT_REGEX))
                    {
                        ErrorLog.appendError(new RowErrorTierOrPrice(
                                             rowNumber,
                                             RowErrorTierOrPrice.PRICE_VALUE,
                                             priceValue,
                                             RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
                        return false;
                    }
                }
                else if(priceType.compareTo(PriceType.PriceTypeValues.Tier.toString()) == 0)
                {
                    if(!priceValue.matches(ValidatorUtils.VALID_TIER_CATEGORY_VALUES_REGEX))
                    {
                        ErrorLog.appendError(new RowErrorTierOrPrice(
                                             rowNumber,
                                             RowErrorTierOrPrice.TIER_VALUE,
                                             priceValue,
                                             RowValidatorTierOrPrice.PRICE_TYPE_PRICE_VALUE));
                        return false;
                    }
                }

                return true; // do not validate against unknown values. cell level checking will catch that.
            }
            catch (NullPointerException NPE) { return true; } // do not validate when the values are null because that means their column wasn't located correctly
        }
        else
            throw new IllegalArgumentException("Unknown EMA version in RowValidatorTierOrPrice");
    }
}
