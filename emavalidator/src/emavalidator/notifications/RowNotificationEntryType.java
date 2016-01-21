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

package emavalidator.notifications;

import emavalidator.AbstractNotificationEntry;

public class RowNotificationEntryType extends AbstractNotificationEntry
{
    public static final String FULL_DELETE_NOTIFICATION_MESSAGE = "\"FullDelete\" EntryType is inputted for the above row(s)";
    public static final String FULL_DELETE_NOTIFICATION_DETAILS = "This means that all line items for this title will be DELETED. Please update your avail before processing if this is incorrect.";

    
    public RowNotificationEntryType(int rowNumber, String notificationMessage, String actualValue, String notificationDetails)
    {
        super(rowNumber, notificationMessage, actualValue, notificationDetails);
    }
}
