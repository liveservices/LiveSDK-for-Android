package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;


/**
 * This class is ran on the UI thread to prevent deadlocking.
 * It adds any operations or exceptions returned to the appropriate
 * blocking queue.
 */
public class CopyAsyncRunnable extends AsyncRunnableWithDestination<LiveOperation,
                                                                    LiveOperationListener> {

    public CopyAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, destination, listener);
    }

    public CopyAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, destination, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        return connectClient.copyAsync(path, destination, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        return connectClient.copyAsync(path, destination, listener, userState);
    }

}
