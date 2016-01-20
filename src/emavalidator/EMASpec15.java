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
 * A concrete representation of EMASpec which can validate an input set of data against the entire EMA 1.5 spec
 * Represents all the rules that data stored in the EMA 1.5 template should follow based on the individual column
 * it's stored in and any other columns that that column references.
 * @author canavan
 */
public class EMASpec15 extends AbstractEMASpec
{
    /**
     * The EMA 1.5 spec has 39 columns. This value is used for validating the length of this.columnSpec vs. the spec itself.
     */
    public static final int NUM_COLUMNS = 39;

    /**
     * The columns that exist only in the 1.5 spec. Used to try to decide which version the user has input.
     * Currently: "AnnounceDate"
     */
    public static final ArrayList<String> UNIQUE_COLUMN_HEADER_VALUES = new ArrayList<String>(Arrays.asList("AnnounceDate"));

    @Override
    protected void buildColumnSpec()
    {
        this.columnSpec.addColumnDefinition(new DisplayName());                                 //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new StoreLanguage());                               //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new Territory());                                   //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new WorkType());                                    //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new EntryType(AbstractEMASpec.EMAVersion.EMASpec15, true)); //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new TitleInternalAlias());                          //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new TitleDisplayUnlimited());                       //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new LicenseType(AbstractEMASpec.EMAVersion.EMASpec15));     //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new LicenseRightsDescription());                    //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new FormatProfile());                               //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new Start());                                       //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new End());                                         //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new PriceType());                                   //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new PriceValue());                                  //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new SRP());                                         //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new Description());                                 //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new OtherTerms());                                  //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new OtherInstructions());                           //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new ContentID());                                   //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new ProductID());                                   //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new EncodeID());                                    //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new AvailID(false));                                //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new Metadata());                                    //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new AltID(true));                                   //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new AnnounceDate());                                //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new ReleaseYear());                                 //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new ReleaseHistoryOriginal());                      //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new ReleaseHistoryPhysicalHV());                    //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new ExceptionFlag());                               //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new RatingSystem());                                //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new RatingValue());                                 //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new RentalDuration());                              //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new WatchDuration());                               //common to EMA 1.4
        this.columnSpec.addColumnDefinition(new CaptionIncluded());                             //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new CaptionRequired());                             //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new Any());                                         //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new ContractID());                                  //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new ServiceProvider());                             //differ to EMA 1.4
        this.columnSpec.addColumnDefinition(new TotalRunTime());                                //common to EMA 1.4

        if(this.columnSpec.getColumnDefinitionSize() != EMASpec15.NUM_COLUMNS) // if the number of columns that we've added to this column spec isn't the same amount we encountered in the external EMA template
            throw new IllegalArgumentException("EMA 1.5 column spec size validation failed.");
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
        this.rowSpec.addValidator(new RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion.EMASpec15));
        this.rowSpec.addValidator(new RowValidatorCaptionIncluded(AbstractEMASpec.EMAVersion.EMASpec15));
        this.rowSpec.addValidator(new RowValidatorCaptionRequired(AbstractEMASpec.EMAVersion.EMASpec15));
//      this.rowSpec.addValidator(new RowValidatorMandatoryRating()); removed because of a feature request. caused too much work for us and partners.
        this.rowSpec.addValidator(new RowValidatorQuestionableStart());
        this.rowSpec.addValidator(new RowValidatorEntryType(AbstractEMASpec.EMAVersion.EMASpec15));
    }

    @Override
    public int getMaximumColumnCount() { return EMASpec15.NUM_COLUMNS; }

    @Override
    public AbstractEMASpec.EMAVersion getEMAVersion() { return AbstractEMASpec.EMAVersion.EMASpec15; }
}
