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

package com.microsoft.live.sample.hotmail;

import org.json.JSONObject;

public class Contact {
    private final JSONObject mContact;

    public Contact(JSONObject contact) {
        assert contact != null;
        mContact = contact;
    }

    public String getId() {
        return mContact.optString("id");
    }

    public String getFirstName() {
        return mContact.isNull("first_name") ? null : mContact.optString("first_name");
    }

    public String getLastName() {
        return mContact.isNull("last_name") ? null : mContact.optString("last_name");
    }

    public String getName() {
        return mContact.optString("name");
    }

    public String getGender() {
        return mContact.isNull("gender") ? null : mContact.optString("gender");
    }

    public boolean getIsFriend() {
        return mContact.optBoolean("is_friend");
    }

    public boolean getIsFavorite() {
        return mContact.optBoolean("is_favorite");
    }

    public String getUserId() {
        return mContact.isNull("user_id") ? null : mContact.optString("user_id");
    }

    public String getUpdatedTime() {
        return mContact.optString("updated_time");
    }

    public JSONObject toJson() {
        return mContact;
    }
}
