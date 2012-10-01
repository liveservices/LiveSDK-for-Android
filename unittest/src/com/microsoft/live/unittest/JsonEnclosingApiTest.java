package com.microsoft.live.unittest;

import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.test.util.AsyncRunnable;
import com.microsoft.live.test.util.AsyncRunnableWithBody;
import com.microsoft.live.test.util.OperationQueueingListener;

public abstract class JsonEnclosingApiTest extends ApiTest<LiveOperation, LiveOperationListener> {
    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, JSONObject body);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, JSONObject body, Object userState);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String body);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
        createAsyncRunnable(String requestPath, String body, Object userState);

    @Override
    protected AsyncRunnable<LiveOperation, LiveOperationListener> createAsyncRunnable(String requestPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected AsyncRunnable<LiveOperation, LiveOperationListener> createAsyncRunnable(String requestPath,
                                                                                      Object userState) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.responseQueue = new LinkedBlockingQueue<LiveOperation>();
        this.queueingListener = new OperationQueueingListener(this.exceptionQueue,
                                                              this.responseQueue);
    }
}
