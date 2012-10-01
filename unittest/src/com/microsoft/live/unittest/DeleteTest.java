package com.microsoft.live.unittest;


import java.io.ByteArrayInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import android.text.TextUtils;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.test.util.DeleteAsyncRunnable;
import com.microsoft.live.test.util.OperationQueueingListener;

public class DeleteTest extends ApiTest<LiveOperation, LiveOperationListener> {

    /** HTTP method this class is testing */
    private static final String METHOD = "DELETE";

    private String calendarId;

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = this.calendarId;
        this.runTestOnUiThread(createAsyncRunnable(requestPath));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();
        LiveOperationException exception = this.pollExceptionQueue();

        this.checkReturnedException(fromMethod, fromCallback, exception);
        this.checkOperationMembers(fromMethod, METHOD, requestPath);
        this.checkResponseBodyInvalid(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValid() throws Throwable {
        this.loadValidResponseBody();

        String requestPath = this.calendarId;
        this.runTestOnUiThread(createAsyncRunnable(requestPath));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, METHOD, requestPath);
        this.checkValidResponseBody(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValidWithUserState() throws Throwable {
        this.loadValidResponseBody();

        Object userState = new Object();
        String requestPath = this.calendarId;
        this.runTestOnUiThread(createAsyncRunnable(requestPath, userState));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, METHOD, requestPath, userState);
        this.checkValidResponseBody(fromMethod);
    }


    @Override
    public void testSyncResponseBodyInvalid() throws Exception {
        this.loadInvalidResponseBody();

        try {
            String requestPath = this.calendarId;
            this.liveConnectClient.delete(requestPath);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertFalse(TextUtils.isEmpty(e.getMessage()));
        }
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadValidResponseBody();

        String requestPath = this.calendarId;
        LiveOperation operation = this.liveConnectClient.delete(requestPath);

        this.checkOperationMembers(operation, METHOD, requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) {
        JSONObject result = operation.getResult();
        assertEquals(0, result.length());

        String rawResult = operation.getRawResult();
        assertEquals("{}", rawResult);
    }

    @Override
    protected DeleteAsyncRunnable createAsyncRunnable(String requestPath) {
        return new DeleteAsyncRunnable(this.responseQueue,
                                       this.liveConnectClient,
                                       requestPath,
                                       this.queueingListener);
    }

    @Override
    protected DeleteAsyncRunnable createAsyncRunnable(String requestPath, Object userState) {
        return new DeleteAsyncRunnable(this.responseQueue,
                                       this.liveConnectClient,
                                       requestPath,
                                       this.queueingListener,
                                       userState);
    }

    @Override
    protected String getMethod() {
        return METHOD;
    }

    @Override
    protected void loadValidResponseBody() {
        // valid delete bodies are empty strings
        String validResponseBody = "";
        this.mockEntity.setInputStream(new ByteArrayInputStream(validResponseBody.getBytes()));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.calendarId = "calendar.013123";
        this.responseQueue = new LinkedBlockingQueue<LiveOperation>();
        this.queueingListener = new OperationQueueingListener(this.exceptionQueue,
                                                              this.responseQueue);
    }
}
