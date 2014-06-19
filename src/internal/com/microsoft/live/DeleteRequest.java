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
