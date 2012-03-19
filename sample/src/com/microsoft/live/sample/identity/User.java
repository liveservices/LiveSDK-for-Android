//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
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
