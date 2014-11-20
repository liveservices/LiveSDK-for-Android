// ------------------------------------------------------------------------------
// Copyright (c) 2014 Microsoft Corporation
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ------------------------------------------------------------------------------

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
