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
