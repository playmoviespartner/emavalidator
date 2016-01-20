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
 * A concrete representation of EMASpec which can validate an input set of data against the entire EMA 1.4 spec
 * Represents all the rules that data stored in the EMA 1.4 template should follow based on the individual column
 * it's stored in and any other columns that that column references.
 * @author canavan
 */
public class EMASpec14 extends AbstractEMASpec
{
    /**
     * The EMA 1.4 spec has 31 columns. This value is used for validating the length of this.columnSpec vs. the spec itself.
     */
    public static final int NUM_COLUMNS = 31;

    /**
     * The columns that exist only in the 1.4 spec. Used to try to decide which version the user has input.
     * Currently: "WSP", "Tier"
     */
    public static final ArrayList<String> UNIQUE_COLUMN_HEADER_VALUES = new ArrayList<String>(Arrays.asList("WSP", "Tier"));

    @Override
    protected void buildColumnSpec()
    {
        this.columnSpec.addColumnDefinition(new DisplayName());
        this.columnSpec.addColumnDefinition(new StoreLanguage());
        this.columnSpec.addColumnDefinition(new Territory());
        this.columnSpec.addColumnDefinition(new WorkType());
        this.columnSpec.addColumnDefinition(new EntryType(AbstractEMASpec.EMAVersion.EMASpec14, false));
        this.columnSpec.addColumnDefinition(new TitleInternalAlias());
        this.columnSpec.addColumnDefinition(new TitleDisplayUnlimited());
        this.columnSpec.addColumnDefinition(new LicenseType(AbstractEMASpec.EMAVersion.EMASpec14));
        this.columnSpec.addColumnDefinition(new LicenseRightsDescription());
        this.columnSpec.addColumnDefinition(new FormatProfile());
        this.columnSpec.addColumnDefinition(new Start());
        this.columnSpec.addColumnDefinition(new End());
        this.columnSpec.addColumnDefinition(new Description());
        this.columnSpec.addColumnDefinition(new OtherTerms());
        this.columnSpec.addColumnDefinition(new OtherInstructions());
        this.columnSpec.addColumnDefinition(new ContentID());
        this.columnSpec.addColumnDefinition(new ProductID());
        this.columnSpec.addColumnDefinition(new AvailID(false));
        this.columnSpec.addColumnDefinition(new Metadata());
        this.columnSpec.addColumnDefinition(new AltID(false));
        this.columnSpec.addColumnDefinition(new ReleaseHistoryOriginal());
        this.columnSpec.addColumnDefinition(new ReleaseHistoryPhysicalHV());
        this.columnSpec.addColumnDefinition(new RentalDuration());
        this.columnSpec.addColumnDefinition(new WatchDuration());
        this.columnSpec.addColumnDefinition(new WSP());
        this.columnSpec.addColumnDefinition(new Tier());
        this.columnSpec.addColumnDefinition(new SRP());
        this.columnSpec.addColumnDefinition(new CaptionIncluded());
        this.columnSpec.addColumnDefinition(new CaptionRequired());
        this.columnSpec.addColumnDefinition(new Any());
        this.columnSpec.addColumnDefinition(new TotalRunTime());

        if(this.columnSpec.getColumnDefinitionSize() != EMASpec14.NUM_COLUMNS) // if the number of columns that we've added to this column spec isn't the same amount we encountered in the external EMA template
            throw new IllegalArgumentException("EMA 1.4 column spec size validation failed.");
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
        this.rowSpec.addValidator(new RowValidatorTierOrPrice(AbstractEMASpec.EMAVersion.EMASpec14));
        this.rowSpec.addValidator(new RowValidatorCaptionIncluded(AbstractEMASpec.EMAVersion.EMASpec14));
        this.rowSpec.addValidator(new RowValidatorCaptionRequired(AbstractEMASpec.EMAVersion.EMASpec14));
        this.rowSpec.addValidator(new RowValidatorQuestionableStart());
    }

    @Override
    public int getMaximumColumnCount() { return EMASpec14.NUM_COLUMNS; }

    @Override
    public AbstractEMASpec.EMAVersion getEMAVersion() { return AbstractEMASpec.EMAVersion.EMASpec14; }
}
