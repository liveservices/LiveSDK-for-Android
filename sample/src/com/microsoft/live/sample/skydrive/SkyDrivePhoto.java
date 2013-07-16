//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.skydrive;

import org.json.JSONArray;
import org.json.JSONObject;

public class SkyDrivePhoto extends SkyDriveObject {

    public static final String TYPE = "photo";

    public static class Image {
        private final JSONObject mImage;

        public Image(JSONObject image) {
            assert image != null;
            mImage = image;
        }

        public int getHeight() {
            return mImage.optInt("height");
        }

        public int getWidth() {
            return mImage.optInt("width");
        }

        public String getSource() {
            return mImage.optString("source");
        }

        public String getType() {
            return mImage.optString("type");
        }

        public JSONObject toJson() {
            return mImage;
        }
    }

    public SkyDrivePhoto(JSONObject photo) {
        super(photo);
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

    public Image[] getImages() {
        JSONArray images = mObject.optJSONArray("images");
        Image[] imgs = new Image[images.length()];
        for (int i = 0; i < images.length(); i++) {
            imgs[i] = new Image(images.optJSONObject(i));
        }

        return imgs;
    }

    public String getWhenTaken() {
        return mObject.isNull("when_taken") ? null : mObject.optString("when_taken");
    }

    public int getHeight() {
        return mObject.optInt("height");
    }

    public int getWidth() {
        return mObject.optInt("width");
    }
}
