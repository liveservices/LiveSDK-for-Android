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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * InputStreamResponseHandler returns an InputStream from an HttpResponse.
 * Singleton--use INSTANCE.
 */
enum InputStreamResponseHandler implements ResponseHandler<InputStream> {
    INSTANCE;

    @Override
    public InputStream handleResponse(HttpResponse response) throws ClientProtocolException,
                                                                    IOException {
        HttpEntity entity = response.getEntity();
        StatusLine statusLine = response.getStatusLine();
        boolean successfulResponse = (statusLine.getStatusCode() / 100) == 2;
        if (!successfulResponse) {
            // If it was not a successful response, the response body contains a
            // JSON error message body. Unfortunately, I have to adhere to the interface
            // and I am throwing an IOException in this case.
            String responseBody = EntityUtils.toString(entity);
            throw new IOException(responseBody);
        }

        return entity.getContent();
    }
}
