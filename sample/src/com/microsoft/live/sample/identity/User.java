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

package com.microsoft.live.sample.identity;

import org.json.JSONObject;

public class User {

    private final JSONObject mUserObj;

    public User(JSONObject userObj) {
        assert userObj != null;
        mUserObj = userObj;
    }

    public String getId() {
        return mUserObj.optString("id");
    }

    public String getName() {
        return mUserObj.optString("name");
    }

    public String getFirstName() {
        return mUserObj.optString("first_name");
    }

    public String getLastName() {
        return mUserObj.optString("last_name");
    }

    public String getLink() {
        return mUserObj.optString("link");
    }

    public int getBirthDay() {
        return mUserObj.optInt("birth_day");
    }

    public int getBirthMonth() {
        return mUserObj.optInt("birth_month");
    }

    public int getBirthYear() {
        return mUserObj.optInt("birth_year");
    }

    public String getGender() {
        return mUserObj.isNull("gender") ? null : mUserObj.optString("gender");
    }

    public String getLocale() {
        return mUserObj.optString("locale");
    }

    public String getUpdatedTime() {
        return mUserObj.optString("updated_time");
    }

    public JSONObject toJson() {
        return mUserObj;
    }
}
