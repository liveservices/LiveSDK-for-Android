package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.LiveOperationException;

public class DownloadOperationQueueingListener extends QueueingListener<LiveDownloadOperation>
                                               implements LiveDownloadOperationListener {

    public DownloadOperationQueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
                                             BlockingQueue<LiveDownloadOperation> responseQueue) {
        super(exceptionQueue, responseQueue);
    }

    @Override
    public void onDownloadCompleted(LiveDownloadOperation operation) {
        this.responseQueue.add(operation);
    }

    @Override
    public void onDownloadFailed(LiveOperationException exception, LiveDownloadOperation operation) {
        this.exceptionQueue.add(exception);
        this.responseQueue.add(operation);
    }

    @Override
    public void onDownloadProgress(int totalBytes,
                                   int bytesRemaining,
                                   LiveDownloadOperation operation) {
        // TODO(skrueger): add support for progress
    }
}
