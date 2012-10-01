package com.microsoft.live.util;

import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;

public enum NullLiveAuthListener implements LiveAuthListener {
    INSTANCE;

    @Override
    public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState) {
        // Do nothing.
    }

    @Override
    public void onAuthError(LiveAuthException exception, Object userState) {
        // Do nothing.
    }
}
