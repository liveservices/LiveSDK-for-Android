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

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

/**
 * CopyRequest is a subclass of a BodyEnclosingApiRequest and performs a Copy request.
 */
class CopyRequest extends EntityEnclosingApiRequest<JSONObject> {

    public static final String METHOD = HttpCopy.METHOD_NAME;

    /**
     * Constructs a new CopyRequest and initializes its member variables.
     *
     * @param session with the access_token
     * @param client to make Http requests on
     * @param path of the request
     * @param entity body of the request
     */
    public CopyRequest(LiveConnectSession session,
                       HttpClient client,
                       String path,
                       HttpEntity entity) {
        super(session, client, JsonResponseHandler.INSTANCE, path, entity);
    }

    /** @return the string "COPY" */
    @Override
    public String getMethod() {
        return METHOD;
    }

    /**
     * Factory method override that constructs a HttpCopy and adds a body to it.
     *
     * @return a HttpCopy with the properly body added to it.
     */
    @Override
    protected HttpUriRequest createHttpRequest() throws LiveOperationException {
        final HttpCopy request = new HttpCopy(this.requestUri.toString());

        request.setEntity(this.entity);

        return request;
    }
}
