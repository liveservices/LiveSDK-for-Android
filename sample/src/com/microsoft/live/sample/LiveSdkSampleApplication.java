//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package com.microsoft.live.sample;

import android.app.Application;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;

public class LiveSdkSampleApplication extends Application {

    private LiveAuthClient mAuthClient;
    private LiveConnectClient mConnectClient;
    private LiveConnectSession mSession;

    public LiveAuthClient getAuthClient() {
        return mAuthClient;
    }

    public LiveConnectClient getConnectClient() {
        return mConnectClient;
    }

    public LiveConnectSession getSession() {
        return mSession;
    }

    public void setAuthClient(LiveAuthClient authClient) {
        mAuthClient = authClient;
    }

    public void setConnectClient(LiveConnectClient connectClient) {
        mConnectClient = connectClient;
    }

    public void setSession(LiveConnectSession session) {
        mSession = session;
    }
}
