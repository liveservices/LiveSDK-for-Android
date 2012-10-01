package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;

public class MoveAsyncRunnable extends AsyncRunnableWithDestination<LiveOperation,
                                                                    LiveOperationListener> {

    public MoveAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, destination, listener);
    }

    public MoveAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, destination, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        return connectClient.moveAsync(path, destination, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        return connectClient.moveAsync(path, destination, listener, userState);
    }

}
