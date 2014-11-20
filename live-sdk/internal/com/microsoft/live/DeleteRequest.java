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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

/**
 * DeleteRequest is a subclass of an ApiRequest and performs a delete request.
 */
class DeleteRequest extends ApiRequest<JSONObject> {

    public static final String METHOD = HttpDelete.METHOD_NAME;

    /**
     * Constructs a new DeleteRequest and initializes its member variables.
     *
     * @param session with the access_token
     * @param client to perform Http requests on
     * @param path of the request
     */
    public DeleteRequest(LiveConnectSession session, HttpClient client, String path) {
        super(session, client, JsonResponseHandler.INSTANCE, path);
    }

    /** @return the string "DELETE" */
    @Override
    public String getMethod() {
        return METHOD;
    }

    /**
     * Factory method override that constructs a HttpDelete request
     *
     * @return a HttpDelete request
     */
    @Override
    protected HttpUriRequest createHttpRequest() {
        return new HttpDelete(this.requestUri.toString());
    }
}
