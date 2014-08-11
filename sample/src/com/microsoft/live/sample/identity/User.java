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
