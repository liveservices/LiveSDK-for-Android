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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * JsonResponseHandler returns a JSONObject from an HttpResponse.
 * Singleton--use INSTANCE.
 */
enum JsonResponseHandler implements ResponseHandler<JSONObject> {
    INSTANCE;

    @Override
    public JSONObject handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {
        final HttpEntity entity = response.getEntity();
        final String stringResponse;
        if (entity != null) {
            stringResponse = EntityUtils.toString(entity);
        } else {
            return null;
        }

        if (TextUtils.isEmpty(stringResponse)) {
            return new JSONObject();
        }

        try {
           return new JSONObject(stringResponse);
        } catch (JSONException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }
}
