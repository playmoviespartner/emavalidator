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
import emavalidator.columns.CollectionAltID;
import emavalidator.columns.CollectionEIDR_URN;
import emavalidator.columns.EpisodeAltID;
import emavalidator.columns.EpisodeContentID;
import emavalidator.columns.EpisodeTitleEIDR_URN;
import emavalidator.columns.SeasonAltID;
import emavalidator.columns.SeasonContentID;
import emavalidator.columns.SeasonEIDR_URN;
import emavalidator.columns.SeriesAltID;
import emavalidator.columns.SeriesContentID;
import emavalidator.columns.SeriesEIDR_URN;
import emavalidator.columns.WorkType;
import emavalidator.errors.RowErrorEIDRValueCheck;

public class RowValidatorSeasonEIDRs extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;
    
    public RowValidatorSeasonEIDRs(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String workType = rowValues.get(WorkType.class.getSimpleName()); // to figure out if we're validating a Season or Episode
            String seriesAltID      = rowValues.get(SeriesAltID.class.getSimpleName());      // required if series EIDR not provided
            String seasonAltID      = rowValues.get(SeasonAltID.class.getSimpleName());      // required if season EIDR not provided
            String episodeAltID     = rowValues.get(EpisodeAltID.class.getSimpleName());     // required if episode EIDR not provided

            boolean validateSuccessfully = true;
            
            // Only EMA Spec 1.7TV changed some column headers.
            if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17TV)
            {
                String seriesEidrID = rowValues.get(SeriesEIDR_URN.class.getSimpleName());  // v1.7 EIDR Series ID
                String seasonEidrID = rowValues.get(SeasonEIDR_URN.class.getSimpleName());  // v1.7 EIDR Season ID
                String episodeEidrID = rowValues.get(EpisodeTitleEIDR_URN.class.getSimpleName());  // v1.7 EIDR Episode ID
                String collectionEidrID = rowValues.get(CollectionEIDR_URN.class.getSimpleName());  // v1.7 EIDR Collection ID
                
                String collectionAltID = rowValues.get(CollectionAltID.class.getSimpleName());  // required if Collection EIDR not provided.
                
                if(seriesEidrID != null && seriesAltID != null && seriesEidrID.isEmpty() && seriesAltID.isEmpty())
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "SeriesEIDR and SeriesAltID are empty"));
                    validateSuccessfully = false;
                }
    
                if(seasonEidrID != null && seasonAltID != null && seasonEidrID.isEmpty() && seasonAltID.isEmpty() && WorkType.isSeason(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "SeasonEIDR and SeasonAltID are empty"));
                    validateSuccessfully = false;
                }
    
                if(episodeEidrID != null && episodeAltID != null && episodeEidrID.isEmpty() && episodeAltID.isEmpty() && WorkType.isEpisode(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "EpisodeEIDR and EpisodeAltID are empty"));
                    validateSuccessfully = false;
                }
                
                if(collectionEidrID != null && collectionAltID != null && collectionEidrID.isEmpty() && collectionAltID.isEmpty() && WorkType.isCollection(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "CollectionEIDR and CollectionAltID are empty"));
                    validateSuccessfully = false;
                }                
            }
            // All other EMA Spec falls through here.
            // 1.4, 1.5, 1.6, 1.6TV
            else 
            {
                String seriesContentID  = rowValues.get(SeriesContentID.class.getSimpleName());  // EIDR series ID
                String seasonContentID  = rowValues.get(SeasonContentID.class.getSimpleName());  // EIDR season ID
                String episodeContentID = rowValues.get(EpisodeContentID.class.getSimpleName()); // EIDR for the episode
    
                if(seriesContentID != null && seriesAltID != null && seriesContentID.isEmpty() && seriesAltID.isEmpty())
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "SeriesContentID and SeriesAltID are empty"));
                    validateSuccessfully = false;
                }
    
                if(seasonContentID != null && seasonAltID != null && seasonContentID.isEmpty() && seasonAltID.isEmpty() && WorkType.isSeason(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "SeasonContentID and SeasonAltID are empty"));
                    validateSuccessfully = false;
                }
    
                if(episodeContentID != null && episodeAltID != null && episodeContentID.isEmpty() && episodeAltID.isEmpty() && WorkType.isEpisode(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, "EpisodeContentID and EpisodeAltID are empty"));
                    validateSuccessfully = false;
                }
            }
            return validateSuccessfully;
        }
        catch(NullPointerException NPE) { return true; } // do not validate when column values are missing
    }
}
