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
import java.util.Iterator;

import emavalidator.columns.UnsupportedColumn;
import emavalidator.errors.CellErrorMissingColumn;
import emavalidator.errors.CellErrorUnsupportedColumn;

/**
 * An instance of a ColumnSpec is purely for aggregating multiple column definitions into a logical collection.
 * A column spec can be thought of as the representation of an entire 'spec'. Used most notably to represent each
 * unique version of the EMA Spec: 1.4, 1.5, and 1.6 currently. Future specs can be added and supported by the
 * validator by creating a new ColumnSpec definition and filling it with column definitions representing each
 * column in the spec.
 * @author canavan
 */
public class ColumnSpec implements Iterable<AbstractColumnDefinition>
{
    /**
     * The list of ColumnDefinitions that were encountered from the input source. Should get successfully reordered to match the input source so that users
     * are not forced to input columns in the exact order that they're defined in the spec.
     */
    private ArrayList<AbstractColumnDefinition> columnDefinitions = new ArrayList<AbstractColumnDefinition>();

    /**
     * Reorganizes the internal ordering of column definitions for this instance of ColumnSpec and makes them
     * match up with the input Column Header definitions
     * @param columnHeaderDefinitions A list of column header names in the order in which they appeared in the input sheet
     * @param rowNumber The row number where the column headers are located. For use in error reporting if columns are not recognized
     */
    public void reorderColumnDefinitions(ArrayList<String> columnHeaderDefinitions, int rowNumber)
    {
        ArrayList<AbstractColumnDefinition> reorderedColumnDefinitions = new ArrayList<AbstractColumnDefinition>();
        for(int x = 0; x < columnHeaderDefinitions.size(); x++)
        {
            AbstractColumnDefinition currentColumn = this.getColumnDefinitionByName(columnHeaderDefinitions.get(x));
            if(currentColumn == null)
            {
                reorderedColumnDefinitions.add(new UnsupportedColumn());
                ErrorLog.appendError(new CellErrorUnsupportedColumn(rowNumber, x, columnHeaderDefinitions.get(x)));
            }
            else
                reorderedColumnDefinitions.add(currentColumn);
        }
        this.columnDefinitions = reorderedColumnDefinitions;
    }

    /**
     * Add a new column definition to this spec. The set of column definitions 'defines' this column spec.
     * @param inputDefinition An instance of a new column definition and all its appropriate validators saved inside
     */
    public void addColumnDefinition(AbstractColumnDefinition inputDefinition) { this.columnDefinitions.add(inputDefinition); }

    /**
     * @param excelColumnLetters The letter code of the column requested. E.G. "A", "BC", "G"
     * @return The requested instance of a column definition located at the given excel column coordinate
     * TODO(canavan) Add support for calling column definitions via Excel alphabet coordinates. E.G. getColumnDefinition("AF")
     */
    public AbstractColumnDefinition getColumnDefinitionAt(String excelColumnLetters)
    {
        throw new UnsupportedOperationException("This function isn't done yet!");
    }

    /**
     * Returns the instance of the column definition represented by the column located at the given column index.
     * This can be useful for retrieving the name of the current column while iterating over this column spec's column definition set.
     * @param inputIndex The index of the column definition to retrieve. Valid values are [0, columnDefinitions.length).
     * @return The requested instance of a column definition located at the given column index.
     */
    public AbstractColumnDefinition getColumnDefinitionAt(int inputIndex) throws IndexOutOfBoundsException
    {
        try
        {
            return this.columnDefinitions.get(inputIndex);
        }
        catch (IndexOutOfBoundsException IOOBE) { return new UnsupportedColumn(); }
    }

    /**
     * @param inputColumnDefinitionName The name of the column definition to retrieve
     * @return The respective ColumnDefinition that represents the String input column Name. Null if none exists in this spec
     */
    public AbstractColumnDefinition getColumnDefinitionByName(String inputColumnDefinitionName)
    {
        for(AbstractColumnDefinition currentColumn : this.columnDefinitions)
            if(currentColumn.compareTo(inputColumnDefinitionName) == 0)
                return currentColumn;
        return null;
    }

    /**
     * Delete all of the column definitions contained inside of this column spec
     * Used for when an invalid column spec has been created and this instance needs to be cleared
     */
    public void clearColumnDefinition()
    {
        this.columnDefinitions.clear();
    }

    /**
     * Returns this column spec's internal column definition's set's size. Useful for creating manual iterators but also
     * useful for things like performing specification verification. E.G. int expectedSize = 10; if expectedSize != spec.getColumnDefinitionSize() ...
     * @return The number of column definitions stored inside this column spec
     */
    public int getColumnDefinitionSize() { return this.columnDefinitions.size(); }

    /**
     * Because for each loops rock. Iterate over all the column definitions, in order, stored in this specific ColumnSpec instance.
     */
    @Override
    public Iterator<AbstractColumnDefinition> iterator()
    {
        return columnDefinitions.listIterator();
    }
    
    public boolean verifyColumnDefinitions(AbstractEMASpec.EMAVersion emaVersion, ArrayList<String> columnHeaderDefinitions)
    {
        ArrayList<String> missingColumns = new ArrayList<String>();
        ColumnSpec cs = AbstractEMASpec.getInstance(emaVersion).getColumnSpec();
        
        // Loop through the spec's column headers and find each column in the inputted column headers
        for (AbstractColumnDefinition currentColumn : cs.columnDefinitions) 
        {
            // Track the missing columns for flagging later.
            if (!columnHeaderDefinitions.contains(currentColumn.getColumnName()))
            {
                missingColumns.add(currentColumn.getColumnName());
            }
        }
        
        // We have some missing columns.
        if (!missingColumns.isEmpty())
        {
            for (String column : missingColumns) 
            {
                ErrorLog.appendError(new CellErrorMissingColumn(column));
            }
        }
        
        return false;
    }
}
