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

import emavalidator.columns.*;
import emavalidator.validators.*;

/**
 * A concrete representation of EMASpec which can validate an input set of data against the entire EMA 1.6 spec
 * Represents all the rules that data stored in the EMA 1.6 template should follow based on the individual column
 * it's stored in and any other columns that that column references.
 * @author canavan
 */
public class EMASpec16 extends AbstractEMASpec
{
    /**
     * The EMA 1.6 spec has 44 columns. This value is used for validating the length of this.columnSpec vs. the spec itself.
     */
    public static final int NUM_COLUMNS = 44;

    /**
     * The columns that exist only in the 1.6 spec. Used to try to decide which version the user has input.
     */
    public static final ArrayList<String> UNIQUE_COLUMN_HEADER_VALUES = new ArrayList<String>(Arrays.asList("LocalizationType", "SuppressionLiftDate", "SpecialPreOrderFulfillDate", "RatingReason", "CaptionExemption", "HoldbackLanguage", "HoldbackExclusionLanguage"));

    @Override
    protected void buildColumnSpec()    {
        this.columnSpec.addColumnDefinition(new DisplayName());                                 //Avail         DisplayName
        this.columnSpec.addColumnDefinition(new StoreLanguage());                               //AvailTrans	StoreLanguage
        this.columnSpec.addColumnDefinition(new Territory());                                   //AvailTrans	Territory
        this.columnSpec.addColumnDefinition(new WorkType());                                    //Avail Asset	WorkType
        this.columnSpec.addColumnDefinition(new EntryType(AbstractEMASpec.EMAVersion.EMASpec16, true)); //Disposition	EntryType
        this.columnSpec.addColumnDefinition(new TitleInternalAlias());                          //AvailMetadata TitleInternalAlias
        this.columnSpec.addColumnDefinition(new TitleDisplayUnlimited());                       //AvailMetadata	TitleDisplayUnlimited
        this.columnSpec.addColumnDefinition(new LocalizationType()); //new to 1.6               //AvailMetadata	LocalizationType
        this.columnSpec.addColumnDefinition(new LicenseType(AbstractEMASpec.EMAVersion.EMASpec16));     //AvailTrans	LicenseType
        this.columnSpec.addColumnDefinition(new LicenseRightsDescription());                    //AvailTrans	LicenseRightsDescription
        this.columnSpec.addColumnDefinition(new FormatProfile());                               //AvailTrans	FormatProfile
        this.columnSpec.addColumnDefinition(new Start());                                       //AvailTrans	Start
        this.columnSpec.addColumnDefinition(new End());                                         //AvailTrans	End
        this.columnSpec.addColumnDefinition(new PriceType());                                   //AvailTrans	PriceType
        this.columnSpec.addColumnDefinition(new PriceValue());                                  //AvailTrans	PriceValue
        this.columnSpec.addColumnDefinition(new SRP());                                         //AvailTrans	SRP
        this.columnSpec.addColumnDefinition(new Description());                                 //AvailTrans	Description
        this.columnSpec.addColumnDefinition(new OtherTerms());                                  //AvailTrans	OtherTerms
        this.columnSpec.addColumnDefinition(new OtherInstructions());                           //AvailTrans	OtherInstructions
        this.columnSpec.addColumnDefinition(new ContentID());                                   //Avail Asset	ContentID
        this.columnSpec.addColumnDefinition(new ProductID());                                   //Avail Asset	ProductID
        this.columnSpec.addColumnDefinition(new EncodeID());                                    //Avail Asset	EncodeID
        this.columnSpec.addColumnDefinition(new AvailID(false));                                //Avail      	AvailID
        this.columnSpec.addColumnDefinition(new Metadata());                                    //Avail Asset   Metadata
        this.columnSpec.addColumnDefinition(new AltID(true));                                   //AvailMetadata AltID
        this.columnSpec.addColumnDefinition(new SuppressionLiftDate()); // new to 1.6           //AvailMetadata SuppressionLiftDate
        this.columnSpec.addColumnDefinition(new SpecialPreOrderFulfillDate()); // new to 1.6    //AvailTrans    SpecialPreOrderFulfillDate
        this.columnSpec.addColumnDefinition(new ReleaseYear());                                 //AvailMetadata	ReleaseYear
        this.columnSpec.addColumnDefinition(new ReleaseHistoryOriginal());                      //AvailMetadata	ReleaseHistoryOriginal
        this.columnSpec.addColumnDefinition(new ReleaseHistoryPhysicalHV());                    //AvailMetadata	ReleaseHistoryPhysicalHV
        this.columnSpec.addColumnDefinition(new ExceptionFlag());                               //Avail      	ExceptionFlag
        this.columnSpec.addColumnDefinition(new RatingSystem());                                //AvailMetadata	RatingSystem
        this.columnSpec.addColumnDefinition(new RatingValue());                                 //AvailMetadata	RatingValue
        this.columnSpec.addColumnDefinition(new RatingReason());                                //AvailMetadata RatingReason
        this.columnSpec.addColumnDefinition(new RentalDuration());                              //AvailTerms	RentalDuration
        this.columnSpec.addColumnDefinition(new WatchDuration());                               //AvailTerms	WatchDuration
        this.columnSpec.addColumnDefinition(new CaptionIncluded());                             //AvailMetadata CaptionIncluded
        this.columnSpec.addColumnDefinition(new CaptionExemption());                            //AvailMetadata	CaptionExtemption
        this.columnSpec.addColumnDefinition(new Any());                                         //AvailMetadata	Any
        this.columnSpec.addColumnDefinition(new ContractID());                                  //AvailTrans	ContractID
        this.columnSpec.addColumnDefinition(new ServiceProvider());                             //Avail      	ServiceProvider
        this.columnSpec.addColumnDefinition(new TotalRunTime());                                //AvailMetadata	TotalRunTime
        this.columnSpec.addColumnDefinition(new HoldbackLanguage());                            //AvailTrans    HoldbackLanguage
        this.columnSpec.addColumnDefinition(new HoldbackExclusionLanguage());                   //AvailTrans    Holdback

        if(this.columnSpec.getColumnDefinitionSize() != EMASpec16.NUM_COLUMNS) // if the number of columns that we've added to this column spec isn't the same amount we encountered in the external EMA template
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
        this.rowSpec.addValidator(new RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion.EMASpec16));
        this.rowSpec.addValidator(new RowValidatorCaptionIncluded(AbstractEMASpec.EMAVersion.EMASpec16));
        this.rowSpec.addValidator(new RowValidatorCaptionExemption(AbstractEMASpec.EMAVersion.EMASpec16));
        this.rowSpec.addValidator(new RowValidatorSuppressionPreorder());
//      this.rowSpec.addValidator(new RowValidatorMandatoryRating()); removed because of a feature request. caused too much work for us and partners.
        this.rowSpec.addValidator(new RowValidatorQuestionableStart());
        this.rowSpec.addValidator(new RowValidatorEntryType(AbstractEMASpec.EMAVersion.EMASpec16));
        this.rowSpec.addValidator(new RowValidatorDuplicate(AbstractEMASpec.EMAVersion.EMASpec16));
        this.rowSpec.addValidator(new RowValidatorOverlappingWindow(AbstractEMASpec.EMAVersion.EMASpec16));
    }

    @Override
    public int getMaximumColumnCount() { return EMASpec16.NUM_COLUMNS; }

    @Override
    public AbstractEMASpec.EMAVersion getEMAVersion() { return AbstractEMASpec.EMAVersion.EMASpec16; }
}
