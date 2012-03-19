//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample.skydrive;

import org.json.JSONObject;

public class SkyDriveFolder extends SkyDriveObject {
    public static final String TYPE = "folder";

    public SkyDriveFolder(JSONObject object) {
        super(object);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getCount() {
        return mObject.optInt("count");
    }
}
