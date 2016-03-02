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

package emavalidator;

import java.util.ArrayList;
import java.util.Arrays;

import emavalidator.columns.Any;
import emavalidator.columns.AvailID;
import emavalidator.columns.CaptionExemption;
import emavalidator.columns.CaptionIncluded;
import emavalidator.columns.CompanyDisplayCredit;
import emavalidator.columns.ContractID;
import emavalidator.columns.Description;
import emavalidator.columns.DisplayName;
import emavalidator.columns.EncodeID;
import emavalidator.columns.End;
import emavalidator.columns.EntryType;
import emavalidator.columns.EpisodeAltID;
import emavalidator.columns.EpisodeContentID;
import emavalidator.columns.EpisodeCount;
import emavalidator.columns.EpisodeNumber;
import emavalidator.columns.EpisodeProductID;
import emavalidator.columns.EpisodeTitleDisplayUnlimited;
import emavalidator.columns.EpisodeTitleInternalAlias;
import emavalidator.columns.ExceptionsFlag;
import emavalidator.columns.FixedEndDate;
import emavalidator.columns.FormatProfile;
import emavalidator.columns.HoldbackExclusionLanguage;
import emavalidator.columns.HoldbackLanguage;
import emavalidator.columns.LicenseRightsDescription;
import emavalidator.columns.LicenseType;
import emavalidator.columns.LocalizationType;
import emavalidator.columns.Metadata;
import emavalidator.columns.OtherInstructions;
import emavalidator.columns.OtherTerms;
import emavalidator.columns.PriceType;
import emavalidator.columns.PriceValue;
import emavalidator.columns.RatingReason;
import emavalidator.columns.RatingSystem;
import emavalidator.columns.RatingValue;
import emavalidator.columns.ReleaseHistoryOriginal;
import emavalidator.columns.ReleaseHistoryPhysicalHV;
import emavalidator.columns.ReleaseYear;
import emavalidator.columns.RentalDuration;
import emavalidator.columns.SRP;
import emavalidator.columns.SeasonAltID;
import emavalidator.columns.SeasonContentID;
import emavalidator.columns.SeasonCount;
import emavalidator.columns.SeasonNumber;
import emavalidator.columns.SeasonTitleDisplayUnlimited;
import emavalidator.columns.SeasonTitleInternalAlias;
import emavalidator.columns.SeriesAltID;
import emavalidator.columns.SeriesContentID;
import emavalidator.columns.SeriesTitleDisplayUnlimited;
import emavalidator.columns.SeriesTitleInternalAlias;
import emavalidator.columns.ServiceProvider;
import emavalidator.columns.SpecialPreOrderFulfillDate;
import emavalidator.columns.Start;
import emavalidator.columns.StoreLanguage;
import emavalidator.columns.SuppressionLiftDate;
import emavalidator.columns.Territory;
import emavalidator.columns.TotalRunTime;
import emavalidator.columns.WatchDuration;
import emavalidator.columns.WorkType;
import emavalidator.validators.RowValidatorCaptionExemption;
import emavalidator.validators.RowValidatorCaptionIncluded;
import emavalidator.validators.RowValidatorDuplicate;
import emavalidator.validators.RowValidatorDuplicateEpisodeNumber;
import emavalidator.validators.RowValidatorEntryType;
import emavalidator.validators.RowValidatorEpisodeNumber;
import emavalidator.validators.RowValidatorQuestionableStart;
import emavalidator.validators.RowValidatorSeasonEIDRs;
import emavalidator.validators.RowValidatorStartLessEnd;
import emavalidator.validators.RowValidatorSuppressionPreorder;
import emavalidator.validators.RowValidatorTierOrPrice;

/**
 * A concrete representation of EMASpec which can validate an input set of data against the entire EMA 1.6 TV spec
 * Represents all the rules that data stored in the EMA 1.6 TV template should follow based on the individual column
 * it's stored in and any other columns that that column references.
 * @author canavan
 */
public class EMASpec16TV extends AbstractEMASpec
{
    /**
     * The EMA 1.6 TV spec has 58 columns. This value is used for validating the length of this.columnSpec vs. the spec itself.
     */
    public static final int NUM_COLUMNS = 58;

    /**
     * The columns that exist only in the 1.6 TV spec. Used to try to decide which version the user has input.
     * Currently: "SeriesTitleInternalAlias", "SeriesTitleDisplayUnlimited", "SeasonNumber", "EpisodeNumber", "EpisodeTitleInternalAlias", "EpisodeTitleDisplayUnlimited", "SeasonTitleInternalAlias", "SeasonTitleDisplayUnlimited", "EpisodeCount", "SeasonCount", "SeriesAltID", "SeasonAltID", "EpisodeAltID", "CompanyDisplayCredit", "SeriesContentID", "SeasonContentID", "EpisodeContentID", "EpisodeProductID", "FixedEndDate"
     */
    public static final ArrayList<String> UNIQUE_COLUMN_HEADER_VALUES = new ArrayList<String>(Arrays.asList("SeriesTitleInternalAlias", "SeriesTitleDisplayUnlimited", "SeasonNumber", "EpisodeNumber", "EpisodeTitleInternalAlias", "EpisodeTitleDisplayUnlimited", "SeasonTitleInternalAlias", "SeasonTitleDisplayUnlimited", "EpisodeCount", "SeasonCount", "SeriesAltID", "SeasonAltID", "EpisodeAltID", "CompanyDisplayCredit", "SeriesContentID", "SeasonContentID", "EpisodeContentID", "EpisodeProductID", "FixedEndDate"));

    @Override
    protected void buildColumnSpec()
    {
        this.columnSpec.addColumnDefinition(new DisplayName());                                   // required, no special symbols
        this.columnSpec.addColumnDefinition(new StoreLanguage());                                 // optional, two digit codes
        this.columnSpec.addColumnDefinition(new Territory());                                     // optional, two digit codes
        this.columnSpec.addColumnDefinition(new WorkType());                                      // required, enumerated values
        this.columnSpec.addColumnDefinition(new EntryType(AbstractEMASpec.EMAVersion.EMASpec16TV, true)); // required, enumerated values
        this.columnSpec.addColumnDefinition(new SeriesTitleInternalAlias());                      // required, no symbols
        this.columnSpec.addColumnDefinition(new SeriesTitleDisplayUnlimited());                   // optional, no symbols
        this.columnSpec.addColumnDefinition(new SeasonNumber());                                  // required, numbers only
        this.columnSpec.addColumnDefinition(new EpisodeNumber());                                 // optional, numbers only, requires row validator
        this.columnSpec.addColumnDefinition(new LocalizationType());                              // optional, enumerated values
        this.columnSpec.addColumnDefinition(new EpisodeTitleInternalAlias());                     // optional, no symbols
        this.columnSpec.addColumnDefinition(new EpisodeTitleDisplayUnlimited());                  // optional, no symbols
        this.columnSpec.addColumnDefinition(new SeasonTitleInternalAlias());                      // optional, no symbols
        this.columnSpec.addColumnDefinition(new SeasonTitleDisplayUnlimited());                   // optional, no symbols
        this.columnSpec.addColumnDefinition(new EpisodeCount());                                  // optional, numbers only
        this.columnSpec.addColumnDefinition(new SeasonCount());                                   // optional, numbers only
        this.columnSpec.addColumnDefinition(new SeriesAltID());                                   // optional, required if no EIDRs, requires row validator
        this.columnSpec.addColumnDefinition(new SeasonAltID());                                   // optional, required if no EIDRs, requires row validator
        this.columnSpec.addColumnDefinition(new EpisodeAltID());                                  // optional, required if no EIDRs, requires row validator
        this.columnSpec.addColumnDefinition(new CompanyDisplayCredit());                          // optional, no symbols
        this.columnSpec.addColumnDefinition(new LicenseType(AbstractEMASpec.EMAVersion.EMASpec16TV));     // required, enumerated values
        this.columnSpec.addColumnDefinition(new LicenseRightsDescription());                      // optional, enumerated values
        this.columnSpec.addColumnDefinition(new FormatProfile());                                 // required, enumerated values
        this.columnSpec.addColumnDefinition(new Start());                                         // required, exhaustively checked
        this.columnSpec.addColumnDefinition(new End());                                           // required, exhaustively checked
        this.columnSpec.addColumnDefinition(new SpecialPreOrderFulfillDate());                    // optional, exhaustively checked
        this.columnSpec.addColumnDefinition(new PriceType());                                     // optional, requires row validator
        this.columnSpec.addColumnDefinition(new PriceValue());                                    // optional, requires row validator
        this.columnSpec.addColumnDefinition(new SRP());                                           // optional, float values only
        this.columnSpec.addColumnDefinition(new Description());                                   // optional, no symbols
        this.columnSpec.addColumnDefinition(new OtherTerms());                                    // optional, no symbols
        this.columnSpec.addColumnDefinition(new OtherInstructions());                             // optional, no symbols
        this.columnSpec.addColumnDefinition(new SeriesContentID());                               // optional, EIDR values only
        this.columnSpec.addColumnDefinition(new SeasonContentID());                               // optional, EIDR values only
        this.columnSpec.addColumnDefinition(new EpisodeContentID());                              // optional, EIDR values only
        this.columnSpec.addColumnDefinition(new EpisodeProductID());                              // optional, EIDR values only
        this.columnSpec.addColumnDefinition(new EncodeID());                                      // optional, EIDR values only
        this.columnSpec.addColumnDefinition(new AvailID(false));                                  // optional, no symbols
        this.columnSpec.addColumnDefinition(new Metadata());                                      // optional, no symbols
        this.columnSpec.addColumnDefinition(new SuppressionLiftDate());                           // optional, exhaustively checked
        this.columnSpec.addColumnDefinition(new ReleaseYear());                                   // optional, year values only
        this.columnSpec.addColumnDefinition(new ReleaseHistoryOriginal());                        // optional, exhaustively checked
        this.columnSpec.addColumnDefinition(new ReleaseHistoryPhysicalHV());                      // optional, exhaustively checked
        this.columnSpec.addColumnDefinition(new ExceptionsFlag());                                 // optional, enumerated values
        this.columnSpec.addColumnDefinition(new RatingSystem());                                  // optional, no symbols
        this.columnSpec.addColumnDefinition(new RatingValue());                                   // optional, no symbols
        this.columnSpec.addColumnDefinition(new RatingReason());                                  // optional, CSV
        this.columnSpec.addColumnDefinition(new RentalDuration());                                // optional, numbers only
        this.columnSpec.addColumnDefinition(new WatchDuration());                                 // optional, numbers only
        this.columnSpec.addColumnDefinition(new FixedEndDate());                                  // optional, exhaustively checked
        this.columnSpec.addColumnDefinition(new CaptionIncluded());                               // optional, required for US, requires row validator
        this.columnSpec.addColumnDefinition(new CaptionExemption());                              // optional, required for US, requires row validator
        this.columnSpec.addColumnDefinition(new Any());                                           // optional, no symbols
        this.columnSpec.addColumnDefinition(new ContractID());                                    // optional, no symbols
        this.columnSpec.addColumnDefinition(new ServiceProvider());                               // optional, no symbols
        this.columnSpec.addColumnDefinition(new TotalRunTime());                                  // optional, decimal values only
        this.columnSpec.addColumnDefinition(new HoldbackLanguage());                              // optional, CSV 2 digit alpha codes
        this.columnSpec.addColumnDefinition(new HoldbackExclusionLanguage());                     // optional, CSV 2 digit alpha codes

        if(this.columnSpec.getColumnDefinitionSize() != EMASpec16TV.NUM_COLUMNS)                // if the number of columns that we've added to this column spec isn't the same amount we encountered in the external EMA template
            throw new IllegalArgumentException("EMA 1.6 column spec size validation failed.");
    }

    @Override
    protected void buildHeaderStrings()
    {
        for(AbstractColumnDefinition currentColumn : this.columnSpec)
            this.specHeaderContents.add(currentColumn.getColumnName());
    }

    @Override
    protected void buildRowSpec()
    {
        this.rowSpec.addValidator(new RowValidatorStartLessEnd());
//      this.rowSpec.addValidator(new RowValidatorExceptionFlagSet()); removed because of a feature request. caused too much work for partners.
        this.rowSpec.addValidator(new RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorCaptionIncluded(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorCaptionExemption(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorSuppressionPreorder());
//      this.rowSpec.addValidator(new RowValidatorMandatoryRating()); removed because of a feature request. caused too much work for us and partners.
        this.rowSpec.addValidator(new RowValidatorSeasonEIDRs(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorEpisodeNumber());
        this.rowSpec.addValidator(new RowValidatorQuestionableStart());
        this.rowSpec.addValidator(new RowValidatorEntryType(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorDuplicate(AbstractEMASpec.EMAVersion.EMASpec16TV));
        this.rowSpec.addValidator(new RowValidatorDuplicateEpisodeNumber(AbstractEMASpec.EMAVersion.EMASpec16TV));
    }

    @Override
    public int getMaximumColumnCount() { return EMASpec16TV.NUM_COLUMNS; }

    @Override
    public AbstractEMASpec.EMAVersion getEMAVersion() { return AbstractEMASpec.EMAVersion.EMASpec16TV; }
}
