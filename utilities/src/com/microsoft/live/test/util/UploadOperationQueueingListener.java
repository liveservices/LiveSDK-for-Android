package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveUploadOperationListener;

public class UploadOperationQueueingListener extends QueueingListener<LiveOperation>
                                             implements LiveUploadOperationListener {

    public UploadOperationQueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
                                           BlockingQueue<LiveOperation> responseQueue) {
        super(exceptionQueue, responseQueue);
    }

    @Override
    public void onUploadCompleted(LiveOperation operation) {
        this.responseQueue.add(operation);
    }

    @Override
    public void onUploadFailed(LiveOperationException exception, LiveOperation operation) {
        this.exceptionQueue.add(exception);
        this.responseQueue.add(operation);
    }

    @Override
    public void onUploadProgress(int totalBytes, int bytesRemaining, LiveOperation operation) {
        // TODO(skrueger): add support for progress
    }
}
