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
import emavalidator.columns.EpisodeAltID;
import emavalidator.columns.EpisodeContentID;
import emavalidator.columns.EpisodeID;
import emavalidator.columns.SeasonAltID;
import emavalidator.columns.SeasonContentID;
import emavalidator.columns.SeasonID;
import emavalidator.columns.SeriesAltID;
import emavalidator.columns.SeriesContentID;
import emavalidator.columns.WorkType;
import emavalidator.errors.RowErrorEIDRValueCheck;

public class RowValidatorEIDRs extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;
    
    public RowValidatorEIDRs(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            String workType = rowValues.get(WorkType.class.getSimpleName()); // to figure out if we're validating a Season or Episode

            boolean validateSuccessfully = true;
            
            if(this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17TV)
            {
                String episodeID     = rowValues.get(EpisodeID.class.getSimpleName());     // required if episode EIDR not provided
                String episodeContentID = rowValues.get(EpisodeContentID.class.getSimpleName());  // v1.7 EIDR Episode ID
//                String alid          = rowValues.get(ALID.class.getSimpleName());
                String seasonID      = rowValues.get(SeasonID.class.getSimpleName());
                String seasonContentID  = rowValues.get(SeasonContentID.class.getSimpleName());
                
                if (WorkType.isEpisode(workType)) {
                    // Fail if both EpisodeID and EpisodeContentID are empty.
                    if (episodeID.isEmpty() && episodeContentID.isEmpty()) {
                        ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                        RowErrorEIDRValueCheck.ERROR_BOTH_EPISODE_IDS_EMPTY,
                                                                        "",
                                                                        RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
                        validateSuccessfully = false;
                    }
                    
                    // Allow pass if either one of the IDs are available.
                }
                
                if (WorkType.isSeason(workType)) {
                    // Fail if both SeasonID and SeasonContentID are empty.
                    if (seasonID.isEmpty() && seasonContentID.isEmpty()) {
                        ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                        RowErrorEIDRValueCheck.ERROR_BOTH_SEASON_IDS_EMPTY,
                                                                        "",
                                                                        RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
                        validateSuccessfully = false;
                    }
                    if (!episodeID.isEmpty()) {
                        ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                        RowErrorEIDRValueCheck.ERROR_EPISODE_ID_CANNOT_HAVE_VALUE,
                                                                        episodeID,
                                                                        RowErrorEIDRValueCheck.EXPECTED_BLANK));
                    }
                    if (!episodeContentID.isEmpty()) {
                        ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber, 
                                                                        RowErrorEIDRValueCheck.ERROR_EPISODE_CONTENT_ID_CANNOT_HAVE_VALUE, 
                                                                        episodeContentID, 
                                                                        RowErrorEIDRValueCheck.EXPECTED_BLANK));
                    }                    
                }
            }
            else if (this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec17) 
            {
                
            }
            // All other EMA Spec falls through here.
            // 1.4, 1.5, 1.6, 1.6TV
            else 
            {
                String seriesAltID      = rowValues.get(SeriesAltID.class.getSimpleName());      // required if series EIDR not provided
                String seasonAltID      = rowValues.get(SeasonAltID.class.getSimpleName());      // required if season EIDR not provided
                String episodeAltID     = rowValues.get(EpisodeAltID.class.getSimpleName());     // required if episode EIDR not provided
                String seriesContentID  = rowValues.get(SeriesContentID.class.getSimpleName());  // EIDR series ID
                String seasonContentID  = rowValues.get(SeasonContentID.class.getSimpleName());  // EIDR season ID
                String episodeContentID = rowValues.get(EpisodeContentID.class.getSimpleName()); // EIDR for the episode
    
                if(seriesContentID != null && seriesAltID != null && seriesContentID.isEmpty() && seriesAltID.isEmpty())
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                    RowErrorEIDRValueCheck.ERROR_NO_EIDR_OR_ID,
                                                                    "SeriesContentID and SeriesAltID are empty", 
                                                                    RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
                    validateSuccessfully = false;
                }
    
                if(seasonContentID != null && seasonAltID != null && seasonContentID.isEmpty() && seasonAltID.isEmpty() && WorkType.isSeason(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                    RowErrorEIDRValueCheck.ERROR_NO_EIDR_OR_ID,
                                                                    "SeasonContentID and SeasonAltID are empty", 
                                                                    RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
                    validateSuccessfully = false;
                }
    
                if(episodeContentID != null && episodeAltID != null && episodeContentID.isEmpty() && episodeAltID.isEmpty() && WorkType.isEpisode(workType))
                {
                    ErrorLog.appendError(new RowErrorEIDRValueCheck(rowNumber,
                                                                    RowErrorEIDRValueCheck.ERROR_NO_EIDR_OR_ID,
                                                                    "EpisodeContentID and EpisodeAltID are empty", 
                                                                    RowErrorEIDRValueCheck.EXPECTED_EIDR_OR_ID));
                    validateSuccessfully = false;
                }
            }
            return validateSuccessfully;
        }
        catch(NullPointerException NPE) { return true; } // do not validate when column values are missing
    }
}
