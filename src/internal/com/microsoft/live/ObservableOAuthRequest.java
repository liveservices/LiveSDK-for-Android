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
 * An OAuth Request that can be observed, by adding observers that will be notified on any
 * exception or response.
 */
interface ObservableOAuthRequest {
    /**
     * Adds an observer to observe the OAuth request
     *
     * @param observer to add
     */
    public void addObserver(OAuthRequestObserver observer);

    /**
     * Removes an observer that is observing the OAuth request
     *
     * @param observer to remove
     * @return true if the observer was removed.
     */
    public boolean removeObserver(OAuthRequestObserver observer);
}
