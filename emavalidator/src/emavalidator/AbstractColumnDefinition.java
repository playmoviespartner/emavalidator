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

/**
 * Implementations of ColumnDefinitions are essentially wrappers for sets of validators.
 * When validation for a ColumnDefinition is performed, each internal validator's validate function is called on the input.
 * ColumnDefinitions and classes that need to refer to ColumnDefinitions by name refer to them via this.getClass().getSimpleName().
 * Each instance of ColumnDefinition needs to provide its own unique implementation of buildValidators().
 * The buildValidators() function is called at construction time and adds the unique validators to each ColumnDefinition at instantiation time.
 * @author canavan
 */
public abstract class AbstractColumnDefinition
{
    /**
     * The list of cell validators to run on every value encountered in the column that this column definition represents.
     */
    protected ArrayList<AbstractCellValidator> validators = new ArrayList<AbstractCellValidator>();
    
    /**
     * Which version of the EMA spec that this ColumnDefinition tries to represent. Can be different from the exact spec due to missing columns or reordered columns.
     */
    protected AbstractEMASpec.EMAVersion emaVersion;
    
    /**
     * Whether or not the value for this ColumnDefinition is required. This changes across columns and EMA versions for each column definition.
     */
    protected boolean required = true;

    /**
     * Constructs an instance of a ColumnDefinition. Makes an implementation-specific call to buildValidators() in order to generate each column definition's set of unique validators
     */
    public AbstractColumnDefinition()                                                {                                                         this.buildValidators(); }

    /**
     * @param emaVersion The exact EMA version that this column comes from. Required only for columns that span multiple EMA versions AND have different definitions for each version
     */
    public AbstractColumnDefinition(AbstractEMASpec.EMAVersion emaVersion)                   { this.emaVersion = emaVersion;                           this.buildValidators(); }

    /**
     * @param required Whether or not the value is required. Used only for columns where values are mandatory in some EMA versions and not in others.
     */
    public AbstractColumnDefinition(boolean required)                                {                               this.required = required; this.buildValidators(); }

    /**
     * @param emaVersion The exact EMA version that this column comes from. Required only for columns that span multiple EMA versions AND have different definitions for each version
     * @param required Whether or not the value is required. Used only for columns where values are mandatory in some EMA versions and not in others.
     */
    public AbstractColumnDefinition(AbstractEMASpec.EMAVersion emaVersion, boolean required) { this.emaVersion = emaVersion; this.required = required; this.buildValidators(); }

    /**
     * Retrieves the case sensitive name of the EMA column that this column definition represents.
     * This can be used for referring to the current column that is being validated.
     * This can also be used for referring to values from a row by name for use in row validation. E.G:
     * hashMap.get(columnDefinitionInstance.getColumnName())
     * hashMap.put(columnDefinitionInstance.getColumnName(), inputValue)
     * @return The case sensitive name of the EMA column that this column definition represents.
     */
    public String getColumnName() { return this.getClass().getSimpleName(); }

    /**
     * Takes an input string at the given coordinates and attempts to run each internally saved validator against it.
     * If all validators pass without error, True is returned. If any of the validators contained within the
     * column definition return a validation error, false is returned. Multiple validation errors can be returned from
     * one input or in other words - validation does not stop when an error is encountered.
     * @param inputString The input to validate against. Usually a cell's contents in string form.
     * @param currentRow The current row that the input was located on.
     * @param currentColumn The current column that the input was located on.
     * @return True if all validators passed successfully, false otherwise.
     */
    public boolean validateInput(String inputString, int currentRow, int currentColumn)
    {
        for (AbstractCellValidator currentValidator : validators)
            if(!currentValidator.validate(inputString, currentRow, currentColumn))
                return false;
        return true;
    }

    /**
     * Lexicographically compares two ColumnDefinitions against each other by using their class name
     * @param otherColumnName The name of another column. Retrieved via ColumnName.getClass().getSimpleName()
     * @return this.getClass().getSimpleName().compareTo(otherColumnName)
     */
    public int compareTo(String otherColumnName)
    {
        return this.getClass().getSimpleName().compareTo(otherColumnName);
    }

    /**
     * Each implementation of ColumnDefinition needs to have a unique implementation of buildValidators().
     * Each unique implementation should add the appropriate validators to the column definition's internal
     * ArrayList<CellValidator> instance. These will all get called at validation time one by one.
     */
    public abstract void buildValidators();
}
