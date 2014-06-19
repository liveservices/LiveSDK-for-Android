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

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of an ObserverableOAuthRequest.
 * Other classes that need to be observed can compose themselves out of this class.
 */
class DefaultObservableOAuthRequest implements ObservableOAuthRequest {

    private final List<OAuthRequestObserver> observers;

    public DefaultObservableOAuthRequest() {
        this.observers = new ArrayList<OAuthRequestObserver>();
    }

    @Override
    public void addObserver(OAuthRequestObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Calls all the Observerable's observer's onException.
     *
     * @param exception to give to the observers
     */
    public void notifyObservers(LiveAuthException exception) {
        for (final OAuthRequestObserver observer : this.observers) {
            observer.onException(exception);
        }
    }

    /**
     * Calls all this Observable's observer's onResponse.
     *
     * @param response to give to the observers
     */
    public void notifyObservers(OAuthResponse response) {
        for (final OAuthRequestObserver observer : this.observers) {
            observer.onResponse(response);
        }
    }

    @Override
    public boolean removeObserver(OAuthRequestObserver observer) {
        return this.observers.remove(observer);
    }
}
