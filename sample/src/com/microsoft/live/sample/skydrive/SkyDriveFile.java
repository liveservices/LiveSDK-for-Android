//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.skydrive;

import org.json.JSONObject;

public class SkyDriveFile extends SkyDriveObject {

    public static final String TYPE = "file";

    public SkyDriveFile(JSONObject file) {
        super(file);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public long getSize() {
        return mObject.optLong("size");
    }

    public int getCommentsCount() {
        return mObject.optInt("comments_count");
    }

    public boolean getCommentsEnabled() {
        return mObject.optBoolean("comments_enabled");
    }

    public String getSource() {
        return mObject.optString("source");
    }

    public boolean getIsEmbeddable() {
        return mObject.optBoolean("is_embeddable");
    }
}
