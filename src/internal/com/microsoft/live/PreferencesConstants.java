//------------------------------------------------------------------------------
// Copyright 2014 Microsoft Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

/**
 * Static class that holds constants used by an application's preferences.
 */
final class PreferencesConstants {
    public static final String COOKIES_KEY = "cookies";

    /** Name of the preference file */
    public static final String FILE_NAME = "com.microsoft.live";

    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    public static final String COOKIE_DELIMITER = ",";

    private PreferencesConstants() { throw new AssertionError(); }
}
