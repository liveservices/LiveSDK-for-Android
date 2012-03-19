//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.skydrive;

import org.json.JSONObject;

public class SkyDriveVideo extends SkyDriveObject {

    public static final String TYPE = "video";

    public SkyDriveVideo(JSONObject object) {
        super(object);
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

    public int getTagsCount() {
        return mObject.optInt("tags_count");
    }

    public boolean getTagsEnabled() {
        return mObject.optBoolean("tags_enabled");
    }

    public String getPicture() {
        return mObject.optString("picture");
    }

    public int getHeight() {
        return mObject.optInt("height");
    }

    public int getWidth() {
        return mObject.optInt("width");
    }

    public int getDuration() {
        return mObject.optInt("duration");
    }

    public int getBitrate() {
        return mObject.optInt("bitrate");
    }
}
