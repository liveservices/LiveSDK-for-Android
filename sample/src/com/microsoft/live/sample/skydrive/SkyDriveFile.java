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
