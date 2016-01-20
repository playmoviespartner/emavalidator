/* Copyright 2015 Google Inc. All Rights Reserved.

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

import emavalidator.AbstractEMASpec;
import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.Window;
import emavalidator.columns.AltID;
import emavalidator.columns.ContentID;
import emavalidator.columns.EncodeID;
import emavalidator.columns.End;
import emavalidator.columns.EntryType;
import emavalidator.columns.EpisodeAltID;
import emavalidator.columns.EpisodeContentID;
import emavalidator.columns.EpisodeNumber;
import emavalidator.columns.EpisodeProductID;
import emavalidator.columns.FormatProfile;
import emavalidator.columns.LicenseType;
import emavalidator.columns.LocalizationType;
import emavalidator.columns.PriceType;
import emavalidator.columns.ProductID;
import emavalidator.columns.SeasonAltID;
import emavalidator.columns.SeasonContentID;
import emavalidator.columns.SeasonNumber;
import emavalidator.columns.SeriesAltID;
import emavalidator.columns.SeriesContentID;
import emavalidator.columns.SeriesTitleInternalAlias;
import emavalidator.columns.SpecialPreOrderFulfillDate;
import emavalidator.columns.Start;
import emavalidator.columns.StoreLanguage;
import emavalidator.columns.SuppressionLiftDate;
import emavalidator.columns.Territory;
import emavalidator.columns.TitleInternalAlias;
import emavalidator.columns.WorkType;
import emavalidator.notifications.RowNotificationOverlappingWindow;

/**
 * This Row Validator is used to find overlapping window row entries.
 * @author ckha
 *
 */
public class RowValidatorOverlappingWindow extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;

    private HashMap<String, ArrayList<Window>> map = new HashMap<String, ArrayList<Window>>();
    
    public RowValidatorOverlappingWindow(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            int actualRowNumber = rowNumber+1;
            String concatString = "";
            ArrayList<String> uniqueColumnsArray = new ArrayList<String>();

            // EMA Version 1.6 Movies
            if(emaVersion == AbstractEMASpec.EMAVersion.EMASpec16)
            {
                // Get all the necessary values from the row to concatenate a UID for Movie.
                String storeLanguage = rowValues.get(StoreLanguage.class.getSimpleName());
                String territory = rowValues.get(Territory.class.getSimpleName());
                String entryType = rowValues.get(EntryType.class.getSimpleName());
                String title = rowValues.get(TitleInternalAlias.class.getSimpleName());
                String localizationType = rowValues.get(LocalizationType.class.getSimpleName());
                String licenseType = rowValues.get(LicenseType.class.getSimpleName());
                String formatProfile = rowValues.get(FormatProfile.class.getSimpleName());
                String priceType = rowValues.get(PriceType.class.getSimpleName());
                String contentID = rowValues.get(ContentID.class.getSimpleName());
                String productID = rowValues.get(ProductID.class.getSimpleName());
                String encodeID = rowValues.get(EncodeID.class.getSimpleName());
                String altID = rowValues.get(AltID.class.getSimpleName());
                String suppressionLiftDate = rowValues.get(SuppressionLiftDate.class.getSimpleName());
                String preorderDate = rowValues.get(SpecialPreOrderFulfillDate.class.getSimpleName());
                
                String[] array = {storeLanguage, territory, entryType, title, localizationType,
                                  licenseType, formatProfile, priceType, contentID, productID,
                                  encodeID, altID, suppressionLiftDate, preorderDate};
                
                // Ignore POEST
                if(!licenseType.equals("POEST")) {
                    for(int i = 0; i < array.length; i++) {
                        uniqueColumnsArray.add(array[i]);
                    }                    
                }
            }
            // EMA Version 1.6 TV
            else if (this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV) 
            {
                // Get all the necessary values from the row to concatenate a UID for Movie.
                String storeLanguage = rowValues.get(StoreLanguage.class.getSimpleName());
                String territory = rowValues.get(Territory.class.getSimpleName());
                String workType = rowValues.get(WorkType.class.getSimpleName());
                String entryType = rowValues.get(EntryType.class.getSimpleName());
                String title = rowValues.get(SeriesTitleInternalAlias.class.getSimpleName());
                String seasonNumber = rowValues.get(SeasonNumber.class.getSimpleName());
                String episodeNumber = rowValues.get(EpisodeNumber.class.getSimpleName());
                String localizationType = rowValues.get(LocalizationType.class.getSimpleName());
                String seriesAltID = rowValues.get(SeriesAltID.class.getSimpleName());
                String seasonAltID = rowValues.get(SeasonAltID.class.getSimpleName());
                String episodeAltID = rowValues.get(EpisodeAltID.class.getSimpleName());
                String licenseType = rowValues.get(LicenseType.class.getSimpleName());
                String formatProfile = rowValues.get(FormatProfile.class.getSimpleName());
                String preorderDate = rowValues.get(SpecialPreOrderFulfillDate.class.getSimpleName());
                String priceType = rowValues.get(PriceType.class.getSimpleName());
                String seriesContentID = rowValues.get(SeriesContentID.class.getSimpleName());
                String seasonContentID = rowValues.get(SeasonContentID.class.getSimpleName());
                String episodeContentID = rowValues.get(EpisodeContentID.class.getSimpleName());
                String episodeProductID = rowValues.get(EpisodeProductID.class.getSimpleName());
                String encodeID = rowValues.get(EncodeID.class.getSimpleName());
                String suppressionLiftDate = rowValues.get(SuppressionLiftDate.class.getSimpleName());
                
                String[] array = {storeLanguage, territory, workType, entryType, title,
                                  seasonNumber, episodeNumber, localizationType, seriesAltID, seasonAltID,
                                  episodeAltID, licenseType, formatProfile, preorderDate, priceType, 
                                  seriesContentID, seasonContentID, episodeContentID, episodeProductID, encodeID,
                                  suppressionLiftDate};
                
                // Ignore POEST
                if(!licenseType.equals("POEST")) {
                    for(int i = 0; i < array.length; i++) {
                        uniqueColumnsArray.add(array[i]);
                    }
                }
            }
            
            // Concatenate all values into a Unique ID String
            concatString = ValidatorUtils.concatUIDString(uniqueColumnsArray);
            
            // Insert the UID String into the hashmap and build Window information
            if(concatString.length() > 0) {
                String start = rowValues.get(Start.class.getSimpleName());
                String end = rowValues.get(End.class.getSimpleName());
                
                // Only try validate on valid start/end inputs.
                if(ValidatorUtils.areValidStartEndDates(start, end)) {
                    int startingDate = ValidatorUtils.convertDateStringToIntValue(start);
                    int endingDate = ValidatorUtils.convertDateStringToIntValue(end);

                    // Ignore "ESTStart" End dates
                    if(end.equals("ESTStart")) 
                    {
                        return false;
                    }
                    // Figure out case for "ESTStart". Ignore for now?

                    Window window = new Window(startingDate, endingDate, new Integer(actualRowNumber));
                    ArrayList<Window> otherWindow = this.map.get(concatString);
                    if (otherWindow == null) {
                        otherWindow = new ArrayList<Window>();
                    }

                    // Check this window with all windows in the array list.
                    for(int i = 0; i < otherWindow.size(); i++) {
                        // If current window's start date is before another window's end date, there may be overlap.
                        if(startingDate < otherWindow.get(i).getEnd()) {
                            // AND: If current window's end date is after another window's start date, there IS overlap.
                            if (endingDate > otherWindow.get(i).getStart()) {
                                ErrorLog.appendNotification(new RowNotificationOverlappingWindow(rowNumber,
                                        RowNotificationOverlappingWindow.OVERLAPPING_WINDOW_FOUND_NOTIFICATION_MESSAGE,
                                        otherWindow.get(i).getRow(),
                                        RowNotificationOverlappingWindow.OVERLAPPING_WINDOW_FOUND_NOTIFICATION_DETAILS));
                                // Break from the loop to only catch this overlap ONCE
                                break;
                            }
                        }
                    }
                    otherWindow.add(window);
                    this.map.put(concatString, otherWindow);
                }
            }
            return false;
        } 
        catch (NullPointerException NPE) { return true; }
    }
}
