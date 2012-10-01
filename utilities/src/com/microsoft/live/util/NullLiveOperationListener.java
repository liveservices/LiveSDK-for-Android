package com.microsoft.live.util;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;

public enum NullLiveOperationListener implements LiveOperationListener {
    INSTANCE;

    @Override
    public void onComplete(LiveOperation operation) {
        // Do nothing.
    }

    @Override
    public void onError(LiveOperationException exception, LiveOperation operation) {
        // Do nothing.
    }
}
