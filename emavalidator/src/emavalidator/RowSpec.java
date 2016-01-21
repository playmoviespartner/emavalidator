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
import java.util.HashMap;

/**
 * A RowSpec represents a set of RowValidators in the same way that a ColumnSpec represents a set of CellValidators.
 * RowSpecs also should contain a mapping of all of that current row's values indexed by their Class.class.getSimpleName() value.
 * It should then, once all row values have been filled out, perform the important role of row validation by delegating each
 * validate call to its internal set of row validators. Each concrete EMA spec implementation has a unique set of RowValidators and thus a unique RowSpec defintion.
 * @author canavan
 */
public class RowSpec
{
    /**
     * The mapping of ColumnName.class.getSimpleName() values mapped to the values that occurred in that row for that Column
     */
    private HashMap<String, String> rowValues = new HashMap<String, String>();

    /**
     * The list of RowValidators to apply to each of the values for each row of input data
     */
    private ArrayList<AbstractRowValidator> rowValidators = new ArrayList<AbstractRowValidator>();

    /**
     * @param key The key to access this value in the row later by. Should be a Class.class.getSimpleName() value.
     * @param value The value that was in the cell indexed by the column name from 'key'
     */
    public void addValue(Object key, Object value) { this.rowValues.put(key.toString(), value.toString()); }

    /**
     * Return the value indexed by the given column name.
     * @param key Should be a Class.class.getSimpleName() value.
     * @return The valued stored in this RowSpec indexed by the column name from 'key'
     */
    public Object getValue(Object key) { return this.rowValues.get(key.toString()); }

    /**
     * Resets the values in this RowSpec and thus the underlying map of keys to values
     */
    public void clearValues() { this.rowValues.clear(); }

    /**
     * Add a new RowValidator implementation to this RowSpec. Each implementation will be called independently at run time via the validate() function.
     * @param rowValidator A new row validator to run on each independent row of input values
     */
    public void addValidator(AbstractRowValidator rowValidator) { this.rowValidators.add(rowValidator); }

    /**
     * Sequentially calls each validator's validate() function that is stored in this RowSpec implementation.
     * @param row The row number that this spec is performing validation on. For use in forwarding to appropriate new ErrorEntry objects, if any.
     * @return The number of errors that occurred during row validation time, if any.
     */
    public int validateRow(int row)
    {
        int errorCount = 0;
        for(AbstractRowValidator currentRowValidator : rowValidators)
        {
            if(!currentRowValidator.validate(rowValues, row))
                errorCount++;
        }
        return errorCount;
    }
}
