package com.microsoft.live.unittest;


import java.io.ByteArrayInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.constants.JsonKeys;
import com.microsoft.live.constants.Paths;
import com.microsoft.live.test.util.GetAsyncRunnable;
import com.microsoft.live.test.util.OperationQueueingListener;

/**
 * Tests all the get operations of the LiveConnectClient.
 */
public class GetTest extends ApiTest<LiveOperation, LiveOperationListener> {

    private static final String METHOD = "GET";

    private static final String USERNAME = "username@live.com";

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = Paths.ME;
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

        String requestPath = Paths.ME;
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
        String requestPath = Paths.ME;
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
            this.liveConnectClient.get(Paths.ME);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadValidResponseBody();

        String requestPath = Paths.ME;
        LiveOperation operation = this.liveConnectClient.get(requestPath);

        this.checkOperationMembers(operation, METHOD, requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) throws JSONException {
        JSONObject response = operation.getResult();
        JSONObject emails = response.getJSONObject(JsonKeys.EMAILS);
        String account = emails.getString(JsonKeys.ACCOUNT);

        assertEquals(USERNAME, account);
    }

    @Override
    protected GetAsyncRunnable createAsyncRunnable(String requestPath) {
        return new GetAsyncRunnable(this.responseQueue,
                                    this.liveConnectClient,
                                    requestPath,
                                    this.queueingListener);
    }

    @Override
    protected GetAsyncRunnable createAsyncRunnable(String requestPath, Object userState) {
        return new GetAsyncRunnable(this.responseQueue,
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
    protected void loadValidResponseBody() throws JSONException {
        String injectedAccount = USERNAME;
        JSONObject injectedEmails = new JSONObject();
        injectedEmails.put(JsonKeys.ACCOUNT, injectedAccount);

        JSONObject injectedResponse = new JSONObject();
        injectedResponse.put(JsonKeys.EMAILS, injectedEmails);

        byte[] bytes = injectedResponse.toString().getBytes();
        this.mockEntity.setInputStream(new ByteArrayInputStream(bytes));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.responseQueue = new LinkedBlockingQueue<LiveOperation>();
        this.queueingListener = new OperationQueueingListener(this.exceptionQueue,
                                                              this.responseQueue);
    }
}
