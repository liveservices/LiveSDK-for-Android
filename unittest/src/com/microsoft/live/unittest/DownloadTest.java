package com.microsoft.live.unittest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.protocol.HTTP;

import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.test.util.DownloadAsyncRunnable;
import com.microsoft.live.test.util.DownloadOperationQueueingListener;

public class DownloadTest extends ApiTest<LiveDownloadOperation, LiveDownloadOperationListener> {

    private static final String VALID_PATH = "file.123/content";
    private static final String RESPONSE = "Some random data";

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = VALID_PATH;
        this.runTestOnUiThread(this.createAsyncRunnable(requestPath));

        LiveDownloadOperation fromMethod = this.responseQueue.take();
        LiveDownloadOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkResponseBodyInvalid(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValid() throws Throwable {
        this.loadValidResponseBody();
        String requestPath = VALID_PATH;

        this.runTestOnUiThread(this.createAsyncRunnable(requestPath));

        LiveDownloadOperation fromMethod = this.responseQueue.take();
        LiveDownloadOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkValidResponseBody(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValidWithUserState() throws Throwable {
        this.loadValidResponseBody();
        String requestPath = VALID_PATH;
        Object userState = new Object();

        this.runTestOnUiThread(this.createAsyncRunnable(requestPath, userState));

        LiveDownloadOperation fromMethod = this.responseQueue.take();
        LiveDownloadOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath, userState);
        this.checkValidResponseBody(fromMethod);

    }


    @Override
    public void testSyncResponseBodyInvalid() throws Exception {
        this.loadInvalidResponseBody();

        String requestPath = VALID_PATH;

        LiveDownloadOperation operation = this.liveConnectClient.download(requestPath);

        this.checkOperationMembers(operation, this.getMethod(), requestPath);
        this.checkResponseBodyInvalid(operation);
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadValidResponseBody();
        String requestPath = VALID_PATH;

        LiveDownloadOperation operation = this.liveConnectClient.download(requestPath);

        this.checkOperationMembers(operation, this.getMethod(), requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveDownloadOperation operation) throws Exception {
        InputStream is = operation.getStream();
        ApiTest.assertEquals(is, RESPONSE);
    }

    @Override
    protected DownloadAsyncRunnable createAsyncRunnable(String requestPath) {
        return new DownloadAsyncRunnable(this.responseQueue,
                                         this.liveConnectClient,
                                         requestPath,
                                         this.queueingListener);
    }

    @Override
    protected DownloadAsyncRunnable createAsyncRunnable(String requestPath, Object userState) {
        return new DownloadAsyncRunnable(this.responseQueue,
                                         this.liveConnectClient,
                                         requestPath,
                                         this.queueingListener,
                                         userState);
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected void loadValidResponseBody() throws Exception {
        byte[] bytes = RESPONSE.getBytes();
        this.mockResponse.addHeader(HTTP.CONTENT_LEN, Long.toString(bytes.length));
        this.mockEntity.setInputStream(new ByteArrayInputStream(bytes));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.responseQueue = new LinkedBlockingQueue<LiveDownloadOperation>();
        this.queueingListener = new DownloadOperationQueueingListener(this.exceptionQueue,
                                                                      this.responseQueue);
    }
}
