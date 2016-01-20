/* Copyright 2015 Google Inc. All Rights Reserved.

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

/**
 * A representation of a row item's avail window, start and end date pair.
 *  
 * @author ckha
 *
 */
public class Window
{
    private int startDate;
    
    private int endDate;
    
    private Integer rowNumber;
    
    /**
     * 
     * @param start - Item start date
     * @param end - Item end date
     * @param row - Row number from the sheet that this pair is found.
     */
    public Window(int start, int end, Integer row)
    {
        this.startDate = start;
        this.endDate = end;
        this.rowNumber = row;
    }
    
    public int getStart()
    {
        return this.startDate;
    }
    public int getEnd() 
    {
        return this.endDate;
    }
    public String getRow()
    {
        return this.rowNumber.toString();
    }
}
