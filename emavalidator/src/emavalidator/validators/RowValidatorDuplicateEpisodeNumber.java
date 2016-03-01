package emavalidator.validators;

import java.util.ArrayList;
import java.util.HashMap;

import emavalidator.AbstractEMASpec;
import emavalidator.AbstractRowValidator;
import emavalidator.ErrorLog;
import emavalidator.columns.End;
import emavalidator.columns.EpisodeNumber;
import emavalidator.columns.FormatProfile;
import emavalidator.columns.LicenseType;
import emavalidator.columns.SeasonNumber;
import emavalidator.columns.SeriesTitleInternalAlias;
import emavalidator.columns.Start;
import emavalidator.columns.Territory;
import emavalidator.columns.WorkType;
import emavalidator.errors.RowErrorDuplicateEpisodeNumber;

public class RowValidatorDuplicateEpisodeNumber extends AbstractRowValidator
{
    private AbstractEMASpec.EMAVersion emaVersion;

    private HashMap<String, String> map = new HashMap<String, String>();
    
    public RowValidatorDuplicateEpisodeNumber(AbstractEMASpec.EMAVersion emaVersion) { this.emaVersion = emaVersion; }

    @Override
    public boolean validate(HashMap<String, String> rowValues, int rowNumber)
    {
        try
        {
            
            String workType = rowValues.get(WorkType.class.getSimpleName());
           
            // Only check for duplicate episode number for WorkType: "Episode".
            if (WorkType.isEpisode(workType)) {
                String episodeNumber = "";
                String concatString = "";
                ArrayList<String> uniqueColumnsArray = new ArrayList<String>();

                // This RowValidator is only used for TV.
                // EMA Version 1.6 TV
                if (this.emaVersion == AbstractEMASpec.EMAVersion.EMASpec16TV) 
                {
                    // Get all the necessary values from the row to concatenate a UID for Movie.
                    String territory = rowValues.get(Territory.class.getSimpleName());
                    String title = rowValues.get(SeriesTitleInternalAlias.class.getSimpleName());
                    String seasonNumber = rowValues.get(SeasonNumber.class.getSimpleName());
                    episodeNumber = rowValues.get(EpisodeNumber.class.getSimpleName());
                    String licenseType = rowValues.get(LicenseType.class.getSimpleName());
                    String formatProfile = rowValues.get(FormatProfile.class.getSimpleName());
                    String start = rowValues.get(Start.class.getSimpleName());
                    String end = rowValues.get(End.class.getSimpleName());
                    
                    String[] array = {territory, title, seasonNumber, episodeNumber, licenseType, formatProfile, start, end};
                    
                    for(int i = 0; i < array.length; i++) {
                        uniqueColumnsArray.add(array[i]);
                    }
                }
                
                // Concatenate all values into a Unique ID String
                concatString = ValidatorUtils.concatUIDString(uniqueColumnsArray);
                
                // Check to make sure the UID String is not already in the static hashmap.
                if(map.get(concatString) != null) {
                    //If found, notify that a duplicate was found.
                    //String episodeNumber = this.map.get(concatString);

                    ErrorLog.appendError(new RowErrorDuplicateEpisodeNumber(rowNumber, 
                                                RowErrorDuplicateEpisodeNumber.DUPLICATE_EPISODE_FOUND_ERROR_MESSAGE,
                                                episodeNumber,
                                                RowErrorDuplicateEpisodeNumber.DUPLICATE_EPISODE_FOUND_ERROR_DETAILS));
                }
                
                // Insert the UID String into the hashmap.
                if(concatString.length() > 0) {
                    this.map.put(concatString, episodeNumber);
                }
            }
            
            return false;
        } 
        catch (NullPointerException NPE) { return true; }

    }
}
