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
 * QueryParameters is a non-instantiable utility class that holds query parameter constants
 * used by the API service.
 */
final class QueryParameters {

    public static final String PRETTY = "pretty";
    public static final String CALLBACK = "callback";
    public static final String SUPPRESS_REDIRECTS = "suppress_redirects";
    public static final String SUPPRESS_RESPONSE_CODES = "suppress_response_codes";
    public static final String METHOD = "method";
    public static final String OVERWRITE = "overwrite";
    public static final String RETURN_SSL_RESOURCES = "return_ssl_resources";

    /** Private to present instantiation. */
    private QueryParameters() {
        throw new AssertionError(ErrorMessages.NON_INSTANTIABLE_CLASS);
    }
}
