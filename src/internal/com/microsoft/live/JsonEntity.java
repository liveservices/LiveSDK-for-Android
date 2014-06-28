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

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * JsonEntity is an Entity that contains a Json body
 */
class JsonEntity extends StringEntity {

    public static final String CONTENT_TYPE = "application/json;charset=" + HTTP.UTF_8;

    /**
     * Constructs a new JsonEntity.
     *
     * @param body
     * @throws UnsupportedEncodingException
     */
    JsonEntity(JSONObject body) throws UnsupportedEncodingException {
        super(body.toString(), HTTP.UTF_8);

        this.setContentType(CONTENT_TYPE);
    }
}
