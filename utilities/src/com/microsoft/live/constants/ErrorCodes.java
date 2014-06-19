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
//------------------------------------------------------------------------------

package com.microsoft.live.constants;

public final class ErrorCodes {

    public static final String REQUEST_PARAMETER_INVALID = "request_parameter_invalid";
    public static final String REQUEST_PARAMETER_MISSING = "request_parameter_missing";
    public static final String REQUEST_URL_INVALID = "request_url_invalid";
    public static final String RESOURCE_NOT_FOUND = "resource_not_found";

    private ErrorCodes() { throw new AssertionError(); }
}
