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

/**
 * An observer of an OAuth Request. It will be notified of an Exception or of a Response.
 */
interface OAuthRequestObserver {
    /**
     * Callback used on an exception.
     *
     * @param exception
     */
    public void onException(LiveAuthException exception);

    /**
     * Callback used on a response.
     *
     * @param response
     */
    public void onResponse(OAuthResponse response);
}
