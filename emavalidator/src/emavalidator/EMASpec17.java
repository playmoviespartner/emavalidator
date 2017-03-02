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

import emavalidator.columns.ALID;
import emavalidator.columns.AllowedLanguages;
import emavalidator.columns.AnnounceDate;
import emavalidator.columns.Any;
import emavalidator.columns.AssetLanguage;
import emavalidator.columns.AvailID;
import emavalidator.columns.BundleALIDs;
import emavalidator.columns.CaptionExemption;
import emavalidator.columns.CaptionIncluded;
import emavalidator.columns.CompanyDisplayCredit;
import emavalidator.columns.ContentID;
import emavalidator.columns.ContractID;
import emavalidator.columns.DMA_ID;
import emavalidator.columns.Description;
import emavalidator.columns.DisplayName;
import emavalidator.columns.EditID;
import emavalidator.columns.End;
import emavalidator.columns.EntryType;
import emavalidator.columns.ExceptionFlag;
import emavalidator.columns.FormatProfile;
import emavalidator.columns.HDR;
import emavalidator.columns.HFR;
import emavalidator.columns.HoldbackLanguage;
import emavalidator.columns.LicenseRightsDescription;
import emavalidator.columns.LicenseType;
import emavalidator.columns.LocalizationType;
import emavalidator.columns.Metadata;
import emavalidator.columns.OtherInstructions;
import emavalidator.columns.OtherTerms;
import emavalidator.columns.PriceCurrency;
import emavalidator.columns.PriceType;
import emavalidator.columns.PriceValue;
import emavalidator.columns.RatingReason;
import emavalidator.columns.RatingSystem;
import emavalidator.columns.RatingValue;
import emavalidator.columns.ReleaseHistoryOriginal;
import emavalidator.columns.ReleaseHistoryPhysicalHV;
import emavalidator.columns.ReleaseYear;
import emavalidator.columns.RentalDuration;
import emavalidator.columns.ReportingID;
import emavalidator.columns.SRP;
import emavalidator.columns.ServiceProvider;
import emavalidator.columns.SpecialPreOrderFulfillDate;
import emavalidator.columns.Start;
import emavalidator.columns.SuppressionLiftDate;
import emavalidator.columns.Territory;
import emavalidator.columns.TitleDisplayUnlimited;
import emavalidator.columns.TitleID;
import emavalidator.columns.TitleInternalAlias;
import emavalidator.columns.TotalRunTime;
import emavalidator.columns.UV_ID;
import emavalidator.columns.WCG;
import emavalidator.columns.WatchDuration;
import emavalidator.columns.WorkType;
import emavalidator.validators.RowValidatorCaptionExemption;
import emavalidator.validators.RowValidatorCaptionIncluded;
import emavalidator.validators.RowValidatorDuplicate;
import emavalidator.validators.RowValidatorEntryType;
import emavalidator.validators.RowValidatorOverlappingWindow;
import emavalidator.validators.RowValidatorQuestionableStart;
import emavalidator.validators.RowValidatorStartLessEnd;
import emavalidator.validators.RowValidatorSuppressionPreorder;
import emavalidator.validators.RowValidatorTierOrPrice;

/**
 * A concrete representation of EMASpec which can validate an input set of data against the entire EMA 1.7 spec
 * Represents all the rules that data stored in the EMA 1.7 template should follow based on the individual column
 * it's stored in and any other columns that that column references.
 * @author ckha
 */
public class EMASpec17 extends AbstractEMASpec
{
    /**
     * The EMA 1.7 spec has 54 columns. This value is used for validating the length of this.columnSpec vs. the spec itself.
     */
    public static final int NUM_COLUMNS = 54;

    /**
     * The columns that exist only in the 1.7 spec. Used to try to decide which version the user has input.
     */
    public static final ArrayList<String> UNIQUE_COLUMN_HEADER_VALUES = 
            new ArrayList<String>(Arrays.asList("TitleID",
                                                "EditID",
                                                "BundleALIDs"));

    @Override
    protected void buildColumnSpec()    {
        this.columnSpec.addColumnDefinition(new DisplayName());                                 //Avail         DisplayName
        this.columnSpec.addColumnDefinition(new AssetLanguage());                               //AvailTrans	AssetLanguage
        this.columnSpec.addColumnDefinition(new Territory());                                   //AvailTrans	Territory
        this.columnSpec.addColumnDefinition(new WorkType());                                    //AvailAsset	WorkType
        this.columnSpec.addColumnDefinition(new EntryType(AbstractEMASpec.EMAVersion.EMASpec17, true)); //Disposition	EntryType
        this.columnSpec.addColumnDefinition(new TitleInternalAlias());                          //AvailMetadata TitleInternalAlias
        this.columnSpec.addColumnDefinition(new TitleDisplayUnlimited());                       //AvailMetadata	TitleDisplayUnlimited
        this.columnSpec.addColumnDefinition(new LocalizationType());                            //AvailMetadata	LocalizationType
        this.columnSpec.addColumnDefinition(new CompanyDisplayCredit());                        //AvailMetadata CompanyDisplayCredit
        this.columnSpec.addColumnDefinition(new LicenseType(AbstractEMASpec.EMAVersion.EMASpec17));     //AvailTrans	LicenseType
        this.columnSpec.addColumnDefinition(new LicenseRightsDescription());                    //AvailTrans	LicenseRightsDescription
        this.columnSpec.addColumnDefinition(new FormatProfile());                               //AvailTrans	FormatProfile
        this.columnSpec.addColumnDefinition(new HDR());                                         //AvailTrans    HDR
        this.columnSpec.addColumnDefinition(new WCG());                                         //AvailTrans    WCG
        this.columnSpec.addColumnDefinition(new HFR());                                         //AvailTrans    HFR
        this.columnSpec.addColumnDefinition(new Start());                                       //AvailTrans    Start
        this.columnSpec.addColumnDefinition(new End());                                         //AvailTrans	End
        this.columnSpec.addColumnDefinition(new PriceType());                                   //AvailTrans	PriceType
        this.columnSpec.addColumnDefinition(new PriceValue());                                  //AvailTrans    PriceValue
        this.columnSpec.addColumnDefinition(new PriceCurrency());                               //AvailTrans    PriceCurrency
        this.columnSpec.addColumnDefinition(new SRP());                                         //AvailTrans	SRP
        this.columnSpec.addColumnDefinition(new Description());                                 //AvailTrans	Description
        this.columnSpec.addColumnDefinition(new OtherTerms());                                  //AvailTrans	OtherTerms
        this.columnSpec.addColumnDefinition(new OtherInstructions());                           //AvailTrans	OtherInstructions
        this.columnSpec.addColumnDefinition(new TitleID());                                     //AvailAsset    TitleID
        this.columnSpec.addColumnDefinition(new EditID());                                      //AvailAsset    EditID
        this.columnSpec.addColumnDefinition(new ContentID());                                   //AvailAsset    ContentID
        this.columnSpec.addColumnDefinition(new AvailID(false));                                //Avail         AvailID
        this.columnSpec.addColumnDefinition(new UV_ID());                                       //Avail         UV_ID
        this.columnSpec.addColumnDefinition(new DMA_ID());                                      //Avail         DMA_ID
        this.columnSpec.addColumnDefinition(new ReportingID());                                 //Avail         ReportingID
        this.columnSpec.addColumnDefinition(new Metadata());                                    //AvailAsset    Metadata
        this.columnSpec.addColumnDefinition(new ALID());                                        //Avail         ALID
        this.columnSpec.addColumnDefinition(new SuppressionLiftDate());                         //AvailTrans    SuppressionLiftDate
        this.columnSpec.addColumnDefinition(new SpecialPreOrderFulfillDate());                  //AvailTrans    SpecialPreOrderFulfillDate
        this.columnSpec.addColumnDefinition(new AnnounceDate());                                //AvailTrans    AnnounceDate
        this.columnSpec.addColumnDefinition(new ReleaseYear());                                 //AvailMetadata	ReleaseYear
        this.columnSpec.addColumnDefinition(new ReleaseHistoryOriginal());                      //AvailMetadata	ReleaseHistoryOriginal
        this.columnSpec.addColumnDefinition(new ReleaseHistoryPhysicalHV());                    //AvailMetadata	ReleaseHistoryPhysicalHV
        this.columnSpec.addColumnDefinition(new ExceptionFlag());                               //Avail      	ExceptionFlag
        this.columnSpec.addColumnDefinition(new RatingSystem());                                //AvailMetadata	RatingSystem
        this.columnSpec.addColumnDefinition(new RatingValue());                                 //AvailMetadata	RatingValue
        this.columnSpec.addColumnDefinition(new RatingReason());                                //AvailMetadata RatingReason
        this.columnSpec.addColumnDefinition(new RentalDuration());                              //AvailTrans	RentalDuration
        this.columnSpec.addColumnDefinition(new WatchDuration());                               //AvailTrans	WatchDuration
        this.columnSpec.addColumnDefinition(new CaptionIncluded());                             //AvailMetadata CaptionIncluded
        this.columnSpec.addColumnDefinition(new CaptionExemption());                            //AvailMetadata	CaptionExtemption
        this.columnSpec.addColumnDefinition(new Any());                                         //AvailMetadata	Any
        this.columnSpec.addColumnDefinition(new ContractID());                                  //AvailTrans	ContractID
        this.columnSpec.addColumnDefinition(new ServiceProvider());                             //Avail      	ServiceProvider
        this.columnSpec.addColumnDefinition(new TotalRunTime());                                //AvailMetadata	TotalRunTime
        this.columnSpec.addColumnDefinition(new HoldbackLanguage());                            //AvailTrans    HoldbackLanguage
        this.columnSpec.addColumnDefinition(new AllowedLanguages());                            //AvailTrans    AllowedLanguages
        this.columnSpec.addColumnDefinition(new BundleALIDs());                                 //Avail         BundleALIDs


        if(this.columnSpec.getColumnDefinitionSize() != EMASpec17.NUM_COLUMNS) // if the number of columns that we've added to this column spec isn't the same amount we encountered in the external EMA template
            throw new IllegalArgumentException("EMA 1.7 column spec size validation failed.");
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
        this.rowSpec.addValidator(new RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion.EMASpec17));
        this.rowSpec.addValidator(new RowValidatorCaptionIncluded(AbstractEMASpec.EMAVersion.EMASpec17));
        this.rowSpec.addValidator(new RowValidatorCaptionExemption(AbstractEMASpec.EMAVersion.EMASpec17));
        this.rowSpec.addValidator(new RowValidatorSuppressionPreorder());
//      this.rowSpec.addValidator(new RowValidatorMandatoryRating()); removed because of a feature request. caused too much work for us and partners.
        this.rowSpec.addValidator(new RowValidatorQuestionableStart());
        this.rowSpec.addValidator(new RowValidatorEntryType(AbstractEMASpec.EMAVersion.EMASpec17));
        this.rowSpec.addValidator(new RowValidatorDuplicate(AbstractEMASpec.EMAVersion.EMASpec17));
        this.rowSpec.addValidator(new RowValidatorOverlappingWindow(AbstractEMASpec.EMAVersion.EMASpec17));
//        this.rowSpec.addValidator(new RowValidatorAltOrProductID());
    }

    @Override
    public int getMaximumColumnCount() { return EMASpec17.NUM_COLUMNS; }

    @Override
    public AbstractEMASpec.EMAVersion getEMAVersion() { return AbstractEMASpec.EMAVersion.EMASpec17; }
}
