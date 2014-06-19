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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * HttpMove represents an HTTP MOVE operation.
 * HTTP MOVE is not a standard HTTP method and this adds it
 * to the HTTP library.
 */
class HttpMove extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "MOVE";

    /**
     * Constructs a new HttpMove with the given uri and initializes its member variables.
     *
     * @param uri of the request
     */
    public HttpMove(String uri) {
        try {
            this.setURI(new URI(uri));
        } catch (URISyntaxException e) {
            final String message = String.format(ErrorMessages.INVALID_URI, "uri");
            throw new IllegalArgumentException(message);
        }
    }

    /** @return the string "MOVE" */
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
