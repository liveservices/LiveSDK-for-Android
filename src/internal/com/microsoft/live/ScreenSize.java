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
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;

/**
 * The ScreenSize is used to determine the DeviceType.
 * Small and Normal ScreenSizes are Phones.
 * Large and XLarge are Tablets.
 */
enum ScreenSize {
    SMALL {
        @Override
        public DeviceType getDeviceType() {
            return DeviceType.PHONE;
        }
    },
    NORMAL {
        @Override
        public DeviceType getDeviceType() {
            return DeviceType.PHONE;
        }

    },
    LARGE {
        @Override
        public DeviceType getDeviceType() {
            return DeviceType.TABLET;
        }
    },
    XLARGE {
        @Override
        public DeviceType getDeviceType() {
            return DeviceType.TABLET;
        }
    };

    public abstract DeviceType getDeviceType();

    /**
     * Configuration.SCREENLAYOUT_SIZE_XLARGE was not provided in API level 9.
     * However, its value of 4 does show up.
     */
    private static final int SCREENLAYOUT_SIZE_XLARGE = 4;

    public static ScreenSize determineScreenSize(Activity activity)  {
        int screenLayout = activity.getResources().getConfiguration().screenLayout;
        int screenLayoutMasked = screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenLayoutMasked) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return SMALL;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return LARGE;
            case SCREENLAYOUT_SIZE_XLARGE:
                return XLARGE;
            default:
                // If we cannot determine the ScreenSize, we'll guess and say it's normal.
                Log.d(
                    "Live SDK ScreenSize",
                    "Unable to determine ScreenSize. A Normal ScreenSize will be returned.");
                return NORMAL;
        }
    }
}
