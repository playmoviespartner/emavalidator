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

package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class RowErrorTierOrPrice extends AbstractErrorEntry
{
    public static final String TIER_OR_PRICE = "Pricing error: PriceType must be specified for each line item.";
    public static final String PRICE_TYPE_VALUE_MISMATCH = "Pricing error: PriceValue must be appropriate for PriceType.";
    // This is a bad check; we should remove it
    public static final String WSP_AND_SRP = "Pricing error: Providing both wholesale price and suggested retail price may cause issues with processing of avails.";
    // This also seems like a bad check (though somewhat unclear as to its condition); please remove
    public static final String TIER_AND_PRICE = "Pricing error: You have entered both a price AND a tier. Only one of these values should be used per line. Please remove one";
    // This is a bad check; Category is a freeform PriceType
    public static final String CATEGORY_VALUE = "Pricing error: You have opted for a Category Price Type requiring your Price Value to be a category designation. Please verify your Price Type and Price Value correspond";
    // 
    public static final String PRICE_VALUE = "Pricing error: The provided PriceType requires a numerical PriceValue.";
    // This seems like a bad check; tier is a freeform PriceType
    public static final String TIER_VALUE = "Pricing error: Your entry for Price Type requires that your Price Value be a tier designation. Please verify your Price Type and Price Value correspond";
    public static final String EMPTY_PRICE_TYPE = "Pricing error: PriceType must be provided for all line items.";
    public static final String EMPTY_PRICE_VALUE = "Pricing error: PriceValue must be provided for all line items.";

    public RowErrorTierOrPrice(int rowNumber, String message, String value, String expected)
    {
        super(rowNumber, message, ErrorLevel.ERROR, value, expected);
    }
}
