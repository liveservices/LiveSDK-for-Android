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


import android.text.TextUtils;

/**
 * LiveConnectUtils is a non-instantiable utility class that contains various helper
 * methods and constants.
 */
final class LiveConnectUtils {

    /**
     * Checks to see if the passed in Object is null, and throws a
     * NullPointerException if it is.
     *
     * @param object to check
     * @param parameterName name of the parameter that is used in the exception message
     * @throws NullPointerException if the Object is null
     */
    public static void assertNotNull(Object object, String parameterName) {
        assert !TextUtils.isEmpty(parameterName);

        if (object == null) {
            final String message = String.format(ErrorMessages.NULL_PARAMETER, parameterName);
            throw new NullPointerException(message);
        }
    }

    /**
     * Checks to see if the passed in is an empty string, and throws an
     * IllegalArgumentException if it is.
     *
     * @param parameter to check
     * @param parameterName name of the parameter that is used in the exception message
     * @throws IllegalArgumentException if the parameter is empty
     * @throws NullPointerException if the String is null
     */
    public static void assertNotNullOrEmpty(String parameter, String parameterName) {
        assert !TextUtils.isEmpty(parameterName);

        assertNotNull(parameter, parameterName);

        if (TextUtils.isEmpty(parameter)) {
            final String message = String.format(ErrorMessages.EMPTY_PARAMETER, parameterName);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Private to prevent instantiation
     */
    private LiveConnectUtils() { throw new AssertionError(ErrorMessages.NON_INSTANTIABLE_CLASS); }
}
