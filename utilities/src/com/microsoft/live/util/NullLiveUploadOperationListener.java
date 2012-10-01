package com.microsoft.live.util;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveUploadOperationListener;

public enum NullLiveUploadOperationListener implements LiveUploadOperationListener {
    INSTANCE;

    @Override
    public void onUploadCompleted(LiveOperation operation) {
        // Do nothing.
    }

    @Override
    public void onUploadFailed(LiveOperationException exception, LiveOperation operation) {
        // Do nothing.
    }

    @Override
    public void onUploadProgress(int totalBytes, int bytesRemaining, LiveOperation operation) {
        // Do nothing.
    }
}
