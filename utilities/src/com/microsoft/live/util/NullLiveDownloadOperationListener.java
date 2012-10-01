package com.microsoft.live.util;

import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.LiveOperationException;

public enum NullLiveDownloadOperationListener implements LiveDownloadOperationListener {
    INSTANCE;

    @Override
    public void onDownloadCompleted(LiveDownloadOperation operation) {
        // Do nothing.
    }

    @Override
    public void onDownloadFailed(LiveOperationException exception,
                                 LiveDownloadOperation operation) {
        // Do nothing.
    }

    @Override
    public void onDownloadProgress(int totalBytes,
                                   int bytesRemaining,
                                   LiveDownloadOperation operation) {
        // Do nothing.
    }
}
