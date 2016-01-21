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

import emavalidator.validators.ValidatorUtils;

/**
 * A class which represents a pair of row and column value. Used to succinctly store the location where an error has occurred during validation.
 * Encompasses the behavior required to print out the location of the error as well.
 * @author canavan
 */
public class RowColPair
{
    /**
     * The row number representing the Y coordinate where this error occurred. 0 index based. Default value is -1 but should always be set to something else before being displayed.
     */
    private int row = -1;

    /**
     * The column number representing the X coordinate where this error occurred. 0 index based. Sentinel value is -1 and means there was no specific row for this error since it most likely spanned multiple columns.
     */
    private int col = -1;

    /**
     * To be used for all errors and the coordinates that they occur on
     * Keep in mind that all ErrorEntry definitions use -1 as the default column value for row errors
     * If invoking this from a row error, the -1 from that column index should propagate correctly to this instance
     * @param row The row the error occurred on
     * @param col The column the error occurred on, or -1 if it didn't occur on a column
     */
    public RowColPair(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row number that this error occurred on. Default value is -1.
     */
    public int getRowNumber() { return this.row; }

    /**
     * @return The column number that this error occurred on. Default value is -1, which is also used for Row errors that have no specific column number
     */
    public int getColumnNumber() { return this.col; }

    /**
     * If there is a valid column value for this RowColPair returns an X,Y coordinate pair: (RX, CY) otherwise returns a single row value: (RX)
     * @return A coordinate pair for column-based errors or a row number only for row validation errors that span multiple columns.
     */
    @Override
    public String toString()
    {
        StringBuilder returnThis = new StringBuilder();
        returnThis.append("(R").append(this.row);
        
        if(this.col != -1)
            returnThis.append(", C").append(this.col); // (RX, CY)
        
        returnThis.append(')');
        return returnThis.toString();
    }

    /**
     * @return The exact same output as this.toString() except with excel coordinates instead of 0th based indexes
     */
    public String toExcelCoordinatesString()
    {
        StringBuilder returnThis = new StringBuilder();
        returnThis.append("(R").append(this.row + 1);
        
        if(this.col != -1)
            returnThis.append(", C").append(this.col + 1);             // (RX, CY)
        
        returnThis.append(')');
        return returnThis.toString();
    }
    
    public String getExcelCoordinatesColumn() 
    {
        StringBuilder returnThis = new StringBuilder(2);
        int index1 = this.col / ValidatorUtils.NUM_CHARACTERS_IN_ALPHABET;
        int index2 = (this.col % ValidatorUtils.NUM_CHARACTERS_IN_ALPHABET) + 1;
        if(index1 > 0) 
            returnThis.append(ValidatorUtils.alphaColumnMapping[index1]);
        returnThis.append(ValidatorUtils.alphaColumnMapping[index2]);
        return returnThis.toString();
    }

    /**
     * @return A new string representing the excel coordinates of this RowColPair's row value only
     */
    public String getExcelCoordinatesRowAsString() { return String.valueOf(this.row + 1); }
    
    /**
     * @return A new integer representing the excel coordinates of this RowColPair's row value only
     */
    public int getExcelCoordinatesRowAsInt() { return this.row + 1; }
    
}
