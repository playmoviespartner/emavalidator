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

package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class RowErrorEpisodeTitleRequired extends AbstractErrorEntry
{
    public static final String ERROR_EPISODE_TITLE = "Episode line items require an episode title.";
    public static final String EXPECTED_EPISODE_TITLE = "Expected a valid string for EpisodeTitleInternalAlias.";

    public RowErrorEpisodeTitleRequired(int rowNumber, String actualValue)
    {
        super(rowNumber, RowErrorEpisodeTitleRequired.ERROR_EPISODE_TITLE, ErrorLevel.ERROR, actualValue, RowErrorEpisodeTitleRequired.EXPECTED_EPISODE_TITLE);
    }
}
