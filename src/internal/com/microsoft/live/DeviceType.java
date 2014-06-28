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

import com.microsoft.live.OAuth.DisplayType;

/**
 * The type of the device is used to determine the display query parameter for login.live.com.
 * Phones have a display parameter of android_phone.
 * Tablets have a display parameter of android_tablet.
 */
enum DeviceType {
    PHONE {
        @Override
        public DisplayType getDisplayParameter() {
            return DisplayType.ANDROID_PHONE;
        }
    },
    TABLET {
        @Override
        public DisplayType getDisplayParameter() {
            return DisplayType.ANDROID_TABLET;
        }
    };

    abstract public DisplayType getDisplayParameter();
}
