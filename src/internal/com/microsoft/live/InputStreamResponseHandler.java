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
