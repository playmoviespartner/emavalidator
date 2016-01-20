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

public class RowErrorEpisodeNumber extends AbstractErrorEntry
{
    public static final String ERROR_EPISODE_NUMBER = "Episode line items require an episode number.";
    public static final String EXPECTED_EPISODE_NUMBER = "Expected a valid number value for EpisodeNumber.";

    public RowErrorEpisodeNumber(int rowNumber, String actualValue)
    {
        super(rowNumber, RowErrorEpisodeNumber.ERROR_EPISODE_NUMBER, ErrorLevel.ERROR, actualValue, RowErrorEpisodeNumber.EXPECTED_EPISODE_NUMBER);
    }
}
