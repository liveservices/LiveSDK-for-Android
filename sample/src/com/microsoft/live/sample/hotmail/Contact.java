//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

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
