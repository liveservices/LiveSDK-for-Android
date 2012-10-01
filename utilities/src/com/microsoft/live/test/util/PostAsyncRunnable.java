package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;

public class PostAsyncRunnable extends AsyncRunnableWithBody<LiveOperation, LiveOperationListener> {

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             JSONObject body,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, body, listener);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             JSONObject body,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, body, listener, userState);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String body,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, body, listener);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String body,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, body, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState(JSONObject body) {
        return connectClient.postAsync(path, body, listener);
    }

    @Override
    protected LiveOperation calledWithoutUserState(String body) {
        return connectClient.postAsync(path, body, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(JSONObject body, Object userState) {
        return connectClient.postAsync(path, body, listener, userState);
    }

    @Override
    protected LiveOperation calledWithUserState(String body, Object userState) {
        return connectClient.postAsync(path, body, listener, userState);
    }

}
