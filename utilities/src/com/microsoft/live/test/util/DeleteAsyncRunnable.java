package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;

public class DeleteAsyncRunnable extends AsyncRunnable<LiveOperation, LiveOperationListener> {

    public DeleteAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               LiveOperationListener listener) {
        super(queue, connectClient, path, listener);
    }

    public DeleteAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               LiveOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        return connectClient.deleteAsync(path, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        return connectClient.deleteAsync(path, listener, userState);
    }
}
