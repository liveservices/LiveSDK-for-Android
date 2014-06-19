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

import android.os.AsyncTask;

/**
 * TokenRequestAsync performs an async token request. It takes in a TokenRequest,
 * executes it, checks the OAuthResponse, and then calls the given listener.
 */
class TokenRequestAsync extends AsyncTask<Void, Void, Void> implements ObservableOAuthRequest {

    private final DefaultObservableOAuthRequest observerable;

    /** Not null if there was an exception */
    private LiveAuthException exception;

    /** Not null if there was a response */
    private OAuthResponse response;

    private final TokenRequest request;

    /**
     * Constructs a new TokenRequestAsync and initializes its member variables
     *
     * @param request to perform
     */
    public TokenRequestAsync(TokenRequest request) {
        assert request != null;

        this.observerable = new DefaultObservableOAuthRequest();
        this.request = request;
    }

    @Override
    public void addObserver(OAuthRequestObserver observer) {
        this.observerable.addObserver(observer);
    }

    @Override
    public boolean removeObserver(OAuthRequestObserver observer) {
        return this.observerable.removeObserver(observer);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            this.response = this.request.execute();
        } catch (LiveAuthException e) {
            this.exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (this.response != null) {
            this.observerable.notifyObservers(this.response);
        } else if (this.exception != null) {
            this.observerable.notifyObservers(this.exception);
        } else {
            final LiveAuthException exception = new LiveAuthException(ErrorMessages.CLIENT_ERROR);
            this.observerable.notifyObservers(exception);
        }
    }
}
