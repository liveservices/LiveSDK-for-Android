package com.microsoft.live.unittest;

import java.util.concurrent.LinkedBlockingQueue;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.test.util.AsyncRunnable;
import com.microsoft.live.test.util.AsyncRunnableWithDestination;
import com.microsoft.live.test.util.OperationQueueingListener;

public abstract class FileOperationApiTest extends ApiTest<LiveOperation, LiveOperationListener> {

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();
        LiveOperationException exception = this.pollExceptionQueue();

        this.checkReturnedException(fromMethod, fromCallback, exception);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkResponseBodyInvalid(fromMethod);

    }

    @Override
    public void testAsyncResponseBodyValid() throws Throwable {
        this.loadValidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkValidResponseBody(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValidWithUserState() throws Throwable {
        this.loadValidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        Object userState = new Object();

        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination, userState));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath, userState);
        this.checkValidResponseBody(fromMethod);
    }

    protected abstract AsyncRunnableWithDestination<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String destination);

    protected abstract AsyncRunnableWithDestination<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String destination, Object userState);

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
