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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import emavalidator.columns.End;
import emavalidator.columns.Start;

public class ValidatorUtils
{

    static
    {
        ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR = new SimpleDateFormat("yyyy-MM-dd");
        ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.setLenient(false); // force non lenient date validation
        ValidatorUtils.ISO8601_DATETIME_FORMAT_VALIDATOR = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    }
    
    public static final int NUM_CHARACTERS_IN_ALPHABET = 26;

    public static final String[] alphaColumnMapping = {"","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    /**
     * Used to validate a chronological date format in YYYY-MM-DD format with respect to things like number of days in a month and leap years / days
     */
    public static SimpleDateFormat CHRONOLOGICAL_DATE_FORMAT_VALIDATOR;
    
    /**
     * 
     */
    public static SimpleDateFormat ISO8601_DATETIME_FORMAT_VALIDATOR;

    /**
     * Regex to allow any String.
     */
    public static final String ANY_NON_BLANK_STRING_REGEX = "(\\S)+";
    
    /**
     * 1, 2, 3, 4, 5, 6
     */
    public static final String CAPTION_EXEMPTION_VALUES_REGEX = "^(1|2|3|4|5|6)$";

    /**
     * Comma, Separated, Values, Only
     */
    public static final String COMMA_SEPARATED_VALUES_REGEX = "^\\w+(, ?\\w+)*$";

    /**
     * Open, ESTStart
     * We do NOT want to accept other values like TBD or in perpetuity
     */
    public static final String ENDING_DATE_VALUES_REGEX = "(?i)^(Open|ESTStart)";

    /**
     * Currently only supporting TBD for now after a length discussion
     * It's not officially part of the EMA spec but we can't get partners to stop using it
     */
    public static final String STARTING_DATE_VALUES_REGEX = "(?i)^(TBD)";

    /**
     * 2014-12-31
     */
    public static final String CHRONOLOGICAL_DATE_FORMAT_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}"; //2014-12-31 NOT 2014/12/31

    /**
     * 1992, 2051
     */
    public static final String YEAR_ONLY_FORMAT_REGEX = "\\d{4}";

    /**
     * 45381, 45381.42832
     */
    public static final String JULIAN_DATE_FORMAT_REGEX = "[0-9]{1,6}(\\.\\d{1,5})?";

    /**
     * 10.5240/1489-49A2-3956-4B2D-FE16-5
     * 10.5240/CB93-339B-33EA-F532-9428-X
     *         0F0D-F5BB-7583-3BB0-53D3-Q
     *         CBA0-60AA-5C46-E70C-A89F-X
     */
    public static final String EIDR_FORMAT_REGEX = "([0-9]{2}\\.[0-9]{4}/)?[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{1,2}";

    /**
     * EMA Avails 1.7
     * urn:eidr:10.5240:7791-8534-2C23-9030-8610-5
     * urn:eidr:10.5240:1489-49A2-3956-4B2D-BEFK-6
     */
    public static final String EIDR_FORMAT_1_7_REGEX = "([0-9]{2}\\.[0-9]{4}/)?[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{1,2}|([\\w]{3}:[\\w]{4}:([0-9]{2}\\.[0-9]{4}:)[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{1,2})|[\\w]+:[\\w]+:[-\\w]+:[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{1,2}";
    
    /**
     * 1, 20, 450, 5810931
     */
    public static final String NUMBER_FORMAT_REGEX = "\\d+";

    /**
     * .3983 .2 .4890234 0.25456
     */
    public static final String DECIMAL_FORMAT_REGEX = "(0)?\\.[0-9]+";

    /**
     * .32 3.43 0.423
     */
    public static final String FLOAT_FORMAT_REGEX = "[0-9]*\\.?[0-9]+";

    /**
     * Used for two digit country codes from the ISO 3166-1 alpha-2 format CA, Ca, ca
     */
    // TODO(ckha): Validate against actual country codes.
    public static final String ISO_TWO_DIGIT_LETTER_CODE_REGEX = "[a-zA-Z]{2}";
    
    /**
     * Used for two digit country codes with region codes from the ISO 3166-1 alpha-2 format fr-CA, en-US
     */
    public static final String ISO_TWO_DIGIT_LETTER_CODE_PLUS_REGION_REGEX = "[a-zA-Z]{2}-[a-zA-Z]{2}";
    
    /**
     * Used for two digit country codes with script codes from the ISO 3166-1 alpha-2 format es-419, zh-Hans
     */
    public static final String ISO_TWO_DIGIT_LETTER_CODE_PLUS_SCRIPT_REGEX = "(?i)^(es-419|zh-Hans|zh-Hant|yue)";

    /**
     * Tab, Carriage Return, Newline
     */
    public static final String ILLEGAL_METADATA_CHARACTERS = "\t\r\n";

    /**
     * !@#$%^&*
     */
    public static final String ILLEGAL_SPECIAL_CHARACTERS = "!@#$%^&*";

    /**
     * Alphanumerics, -_.@/
     */
    public static final String VALID_ALTID_REGEX = "[a-zA-Z0-9-_.@/]*";
    public static final String VALID_GENERIC_ID_REGEX = "[a-zA-Z0-9-_.@/:]*";

    /**
     * 1, 13, A, CBA, Tier 1, Tier1, Tier-1, TierB, Tier-B, T 1
     * DO_NOT_SELL is a valid tier category for TV EMA Avails v1.7
     */
    //TODO(canavan) rethink this, partners have weird tier names
    public static final String VALID_TIER_CATEGORY_VALUES_REGEX = "do_not_sell|[a-zA-Z0-9 ]*( |_|-|\\+)?[a-zA-Z0-9]*";

    /**
     * Yes, YES, Y, y No, NO, N, n
     */
    public static final String YES_OR_NO_ONLY_REGEX = "(?i)^(yes|y|no|n)$";


    /**
     * YES, Yes, yes, y
     */
    public static final String YES_REGEX = "(?i)^(yes|y)$";

    /**
     * NO, No, no, n
     */
    public static final String NO_REGEX = "(?i)^(no|n)$";

    /**
     * US, us, Us
     */
    public static final String UNITED_STATES_COUNTRY_CODE_VALUE_REGEX = "(?i)^US$";

    /**
     * Movie, Short, Season, Episode
     */
    public static final String WORK_TYPE_VALUES_REGEX = "(?i)^(movie|short|season|episode|collection|episode:collection member|season:collection member|series:miniseries)$";

    /**
     * HD, SD, 3D, 3DHD, 3DSD, HFR, 3DHFR, 4K, 3D4K
     */
    public static final String FORMAT_PROFILE_VALUES_REGEX = "(?i)^(SD|HD|UHD|3D|3DSD|3DHD|3DUHD)$";

    /**
     * New Release, Library, Mega-Library, DD-Theatrical, Pre-Theatrical, Early EST, Preorder EST, Early VOD, Preorder VOD, DTV, DD-DVD, Next Day TV, POD
     * We do NOT want to accept catalog as a valid value
     */
    public static final String LICENSE_RIGHTS_DESCRIPTION_VALUES_REGEX = "(?i)^(New ?Release|(Priority)?(-| )?Library|Mega(-| )?Library|DD(-| )?Theatrical|Pre(-| )?Theatrical|Early(-| )?EST|Preorder(-| )?EST|Early(-| )?VOD|Preorder(-| )?VOD|Next ?Day ?TV|Season ?Only|DTV|DD-DVD|Free|POD|DO_NOT_SELL)$";

    /**
     * EST, VOD, SVOD
     */
    public static final String LICENSE_TYPE_VALUES_REGEX_14_15 = "(?i)^(EST|VOD|SVOD)$";

    /**
     * EST, VOD, SVOD, POEST
     */
    public static final String LICENSE_TYPE_VALUES_REGEX_16 = "(?i)^(EST|VOD|SVOD|POEST)$";
    
    /**
     * The value for pre order EST for the license type column in 1.6 EMA+
     */
    public static final String LICENSE_TYPE_PRE_ORDER_EST = "POEST";

    /**
     * Sub, Dub, Subdub, Any
     */
    public static final String LOCALIZATION_TYPE_VALUES_REGEX = "(?i)^(SUB|SUBBED|DUB|DUBBED|SUBDUB|ANY)$";

    /**
     * "Full Extract", "Create", "Update", "Delete"
     */
    public static final String ENTRY_TYPE_VALUES_REGEX_14 = "(?i)^(Create|Update|Delete|Full Extract)$";

    /**
     * "Full Extract", "Full Delete"
     */
    public static final String ENTRY_TYPE_VALUES_REGEX_15_16 = "(?i)^(Full Extract|Full Delete)$";

    /**
     * Should match only the empty string and all white spaces
     */
    public static final String EMPTY_STRING_REGEX = "^[ \t]*$";

    /**
     * Tier, WSP, SRP, or Category
     */
    public static final String PRICE_TYPE_VALUES_REGEX = "(?i)^(Tier|WSP|SRP|Category)$";

    public static final String EXPECTED_EIDR_VALUES = "Examples: 10.5240/CB93-339B-33EA-F532-9428-X, 0F0D-F5BB-7583-3BB0-53D3-Q";

    public static final String EXPECTED_EIDR_1_7_VALUES = "Examples: urn:eidr:10.5240:1489-49A2-3956-4B2D-BEFK-6";
    
    public static final String EXPECTED_TWO_DIGIT_ISO_CODES = "CA, US, FR, GB, etc. (en-US, fr-CA, zh-Hans, also allowed).";

    /**
     * USD, CAD, JPY, EUR
     */
    public static final String PRICE_CURRENCY_VALUES_REGEX = "[A-Z]{3}";
    
    /**
     * 32, 0:32, 12:32, 1:45
     */
    public static final String TIME_FORMAT_REGEX = "(\\d{1,}:)?\\d{2}:?\\d{2}";

    /**
     * True, False, DV, HDR10
     */
    public static final String HDR_VALUES_REGEX = "(?i)^(True|False|DV|HDR10)$";
    
    /**
     * True, False
     */
    public static final String WCG_VALUES_REGEX = "(?i)^(True|False)$";
    
    /**
     * True, False
     */
    public static final String HFR_VALUES_REGEX = "(?i)^(True|False)$";
    
    /**
     * True, False, Atmos, DTS:X, Auro3D
     */
    public static final String NGAUDIO_VALUES_REGEX = "(?i)^(True|False|Atmos|DTS:X|Auro3D)$";
    
    public static boolean equals(final String s1, final String s2)
    {
        return s1 != null && s2 != null && s1.hashCode() == s2.hashCode() && s1.equals(s2);
    }

    /**
     * @param dateClassName The name of the class containing the date value. Examples are Start, End
     * @param inputString The actual date value to validate. Can be any string value
     * @return True if the start or end date is valid according to its parent column's rules, false otherwise.
     */
    public static boolean isValidStartEndDate(String dateClassName, String inputString)
    {
        if(inputString.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX))
            return true;

        if(dateClassName.compareTo(End.class.getSimpleName()) == 0 &&
           inputString.matches(ValidatorUtils.ENDING_DATE_VALUES_REGEX))
            return true;

        if(dateClassName.compareTo(Start.class.getSimpleName()) == 0 &&
           inputString.matches(ValidatorUtils.STARTING_DATE_VALUES_REGEX))
            return true;

        try
        {
            // Length greater than 10 means it may be in ISO-8601 format.
            if (inputString.length() > 10) {
                ValidatorUtils.ISO8601_DATETIME_FORMAT_VALIDATOR.parse(inputString);
            }
            // Otherwise, assume YYYY-mm-DD Date String.
            else {
                ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.parse(inputString);
            }
            return true;
        }
        catch (ParseException e) {
            System.out.println(e);
        }

        return false;
    }

    /**
     * Specifically only validates avail Start VS. avail End.
     * @param startDate The starting date value. Can be any valid EMA symbol, julian, or YYYY-MM-DD date value
     * @param endDate The ending date value. Can be any valid EMA symbol, julian, or YYYY-M-DD date value
     * @return True if the two dates can be correctly evaluated against each other, false otherwise.
     */
    public static boolean areValidStartEndDates(String startDate, String endDate)
    {
        if(!ValidatorUtils.isValidStartEndDate(Start.class.getSimpleName(), startDate) ||
           !ValidatorUtils.isValidStartEndDate(End.class.getSimpleName(), endDate))
               return false; // if neither individual value is valid, return false before attempting to compare them to each other

        if(startDate.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX) &&
             endDate.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX))
             return Double.valueOf(endDate) > Double.valueOf(startDate); // this should be type safe as the only values that should get matched here should not throw NumberFormatException

        else if(startDate.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX) &&
                  endDate.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX))
        {
            try
            {
                Date startingDate = ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.parse(startDate);
                Date   endingDate = ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.parse(endDate);
                return startingDate.compareTo(endingDate) < 0;
            }
            catch (ParseException e) { return false; }
        }
        return true;
    }

    public static boolean isQuestionableStartDate(String startDate, String releaseDate)
    {
        if(releaseDate.isEmpty())
            return false;

        if(startDate.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX) &&
                releaseDate.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX))
            return Double.valueOf(startDate) < Double.valueOf(releaseDate);

        else if(startDate.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX) &&
                releaseDate.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX))
        {
            try
            {
                Date startingDate = ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.parse(startDate);
                Date   releasingDate = ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_VALIDATOR.parse(releaseDate);
                return startingDate.compareTo(releasingDate) < 0;
            }
            catch (ParseException e) { return false; }
        }
        return false;
    }
    
    public static Integer convertDateStringToIntValue(String dateString) {
        int returnDate = -1;
        
        // "Open" means no end. Set the date to something large for comparison.
        if(dateString.equals("Open")) {
            return Integer.parseInt("99999999");
        }
        
        try
        {
            if(dateString.matches(ValidatorUtils.JULIAN_DATE_FORMAT_REGEX)) 
            {
                returnDate = Integer.parseInt(dateString);
            }
            else if(dateString.matches(ValidatorUtils.CHRONOLOGICAL_DATE_FORMAT_REGEX))
            {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                returnDate = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(date));
            }
        } catch (ParseException e) { return -1; }
        return returnDate;
    }
    
    /**
     * Concatenate a Unique ID for a row from selected column values to use in a check for duplicate values within the validating Sheet.
     * @param strArray array of column values to concatenate into a Unique ID. 
     * @return The concatenated Unique ID String.
     */
    public static String concatUIDString(ArrayList<String> strArray)
    {
        StringBuilder returnThis = new StringBuilder(100);
        for(int i = 0; i < strArray.size(); i++) {
            returnThis.append(strArray.get(i));
        }
        return returnThis.toString();
    }
}
