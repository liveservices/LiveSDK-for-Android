package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;

public abstract class AsyncRunnableWithDestination<OperationType, ListenerType>
        extends AsyncRunnable<OperationType, ListenerType> {

    protected final String destination;

    public AsyncRunnableWithDestination(BlockingQueue<OperationType> queue,
                                        LiveConnectClient connectClient,
                                        String path,
                                        String destination,
                                        ListenerType listener) {
        super(queue, connectClient, path, listener);
        this.destination = destination;
    }

    public AsyncRunnableWithDestination(BlockingQueue<OperationType> queue,
                                        LiveConnectClient connectClient,
                                        String path,
                                        String destination,
                                        ListenerType listener,
                                        Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.destination = destination;
    }

}
