package com.microsoft.live.unittest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONObject;

import android.text.TextUtils;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveUploadOperationListener;
import com.microsoft.live.OverwriteOption;
import com.microsoft.live.constants.JsonKeys;
import com.microsoft.live.constants.Paths;
import com.microsoft.live.mock.MockHttpEntity;
import com.microsoft.live.mock.MockHttpResponse;
import com.microsoft.live.test.util.UploadAsyncRunnable;
import com.microsoft.live.test.util.UploadOperationQueueingListener;
import com.microsoft.live.util.NullLiveUploadOperationListener;

public class UploadTest extends ApiTest<LiveOperation, LiveUploadOperationListener> {

    private static final String SOURCE = "http://download.location.com/some/path";
    private static final InputStream FILE;
    private static final String FILE_ID = "file.1231";
    private static final String FILENAME = "some_file.txt";

    static {
        FILE = new ByteArrayInputStream("File contents".getBytes());
    }

    public void testAsyncFileNull() {
        try {
            this.liveConnectClient.uploadAsync(Paths.ME_SKYDRIVE,
                                               null,
                                               FILE,
                                               NullLiveUploadOperationListener.INSTANCE);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testAsyncFilenameNull() {
        try {
            this.liveConnectClient.uploadAsync(Paths.ME_SKYDRIVE,
                                               FILENAME,
                                               (InputStream)null,
                                               NullLiveUploadOperationListener.INSTANCE);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }

        try {
            this.liveConnectClient.uploadAsync(Paths.ME_SKYDRIVE,
                                               FILENAME,
                                               (File)null,
                                               NullLiveUploadOperationListener.INSTANCE);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;
        this.runTestOnUiThread(createAsyncRunnable(requestPath, FILENAME, FILE));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();
        LiveOperationException exception = this.pollExceptionQueue();

        this.checkReturnedException(fromMethod, fromCallback, exception);
        this.checkOperationMembers(fromMethod, getMethod(), requestPath);
        this.checkResponseBodyInvalid(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValid() throws Throwable {
        this.loadUploadLocationResponseBody();
        this.loadValidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;

        this.runTestOnUiThread(createAsyncRunnable(requestPath, FILENAME, FILE));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, getMethod(), requestPath);
        this.checkValidResponseBody(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValidWithUserState() throws Throwable {
        this.loadUploadLocationResponseBody();
        this.loadValidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;
        Object userState = new Object();

        this.runTestOnUiThread(createAsyncRunnable(requestPath, FILENAME, FILE, userState));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, getMethod(), requestPath, userState);
        this.checkValidResponseBody(fromMethod);
    }

    public void testAsyncResponseBodyValidWithOverwrite() throws Throwable {
        this.loadUploadLocationResponseBody();
        this.loadValidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;
        Object userState = new Object();

        this.runTestOnUiThread(createAsyncRunnable(
                requestPath,
                FILENAME,
                OverwriteOption.Overwrite,
                FILE,
                userState));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, getMethod(), requestPath, userState);
        this.checkValidResponseBody(fromMethod);
    }

    public void testSyncFileNull() throws Exception {
        try {
            this.liveConnectClient.upload(Paths.ME_SKYDRIVE, FILENAME, (InputStream)null);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }

        try {
            this.liveConnectClient.upload(Paths.ME_SKYDRIVE, FILENAME, (File)null);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testSyncFilenameNull() throws Exception {
        try {
            this.liveConnectClient.upload(Paths.ME_SKYDRIVE, null, FILE);
            this.failNoNullPointerExceptionThrown();
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Override
    public void testSyncResponseBodyInvalid() throws Exception {
        this.loadInvalidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;

        try {
            this.liveConnectClient.upload(requestPath, FILENAME, FILE);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertFalse(TextUtils.isEmpty(e.getMessage()));
        }
    }

    public void testSyncResponseBodyInvalidWithOverwrite() throws Exception {
        this.loadInvalidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;

        try {
            this.liveConnectClient.upload(requestPath, FILENAME, FILE, OverwriteOption.Overwrite);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertFalse(TextUtils.isEmpty(e.getMessage()));
        }
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadUploadLocationResponseBody();
        this.loadValidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;

        LiveOperation operation = this.liveConnectClient.upload(requestPath, FILENAME, FILE);

        this.checkOperationMembers(operation, getMethod(), requestPath);
        this.checkValidResponseBody(operation);
    }

    public void testSyncResponseBodyValidWithOverwrite() throws Exception {
        this.loadUploadLocationResponseBody();
        this.loadValidResponseBody();

        String requestPath = Paths.ME_SKYDRIVE;

        LiveOperation operation = this.liveConnectClient.upload(
                requestPath,
                FILENAME,
                FILE,
                OverwriteOption.Overwrite);

        this.checkOperationMembers(operation, getMethod(), requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) throws Exception {
        JSONObject result = operation.getResult();

        assertEquals(2, result.length());

        String id = result.getString(JsonKeys.ID);
        assertEquals(FILE_ID, id);

        String source = result.getString(JsonKeys.SOURCE);
        assertEquals(SOURCE, source);
    }

    protected UploadAsyncRunnable createAsyncRunnable(String requestPath,
                                                      String filename,
                                                      InputStream file) {
        return new UploadAsyncRunnable(this.responseQueue,
                                       this.liveConnectClient,
                                       requestPath,
                                       filename,
                                       file,
                                       this.queueingListener);
    }

    protected UploadAsyncRunnable createAsyncRunnable(String requestPath,
                                                      String filename,
                                                      InputStream file,
                                                      Object userState) {
        return new UploadAsyncRunnable(this.responseQueue,
                                       this.liveConnectClient,
                                       requestPath,
                                       filename,
                                       file,
                                       this.queueingListener,
                                       userState);
    }

    protected UploadAsyncRunnable createAsyncRunnable(String requestPath,
                                                      String filename,
                                                      OverwriteOption overwrite,
                                                      InputStream file,
                                                      Object userState) {
        return new UploadAsyncRunnable(this.responseQueue,
                                       this.liveConnectClient,
                                       requestPath,
                                       filename,
                                       overwrite,
                                       file,
                                       this.queueingListener,
                                       userState);
    }

    @Override
    protected final UploadAsyncRunnable createAsyncRunnable(String requestPath) {
        throw new UnsupportedOperationException("Unable to create UploadAsyncRunnable from only " +
                                                "a requestPath");
    }

    @Override
    protected final UploadAsyncRunnable createAsyncRunnable(String requestPath, Object userState) {
        throw new UnsupportedOperationException("Unable to create UploadAsyncRunnable from only " +
                                                "a requestPath and an userState");
    }

    @Override
    protected void loadInvalidResponseBody() throws Exception {
        super.loadInvalidResponseBody();

        HttpResponse invalidResponse = this.mockClient.getHttpResponse();

        this.mockClient.clearHttpResponseQueue();
        this.loadUploadLocationResponseBody();
        this.mockClient.addHttpResponse(invalidResponse);
    }

    @Override
    protected void loadPathInvalidResponse(String requestPath) throws Exception {
        super.loadPathInvalidResponse(requestPath);

        // we have to load the uploadLocationResponse first
        // so store the invalidResponse and load it in again after the uploadlocation
        // has been added.
        HttpResponse invalidResponse = this.mockClient.getHttpResponse();

        this.mockClient.clearHttpResponseQueue();
        this.loadUploadLocationResponseBody();
        this.mockClient.addHttpResponse(invalidResponse);
    }

    @Override
    protected void loadValidResponseBody() throws Exception {
        JSONObject jsonResponseBody = new JSONObject();
        jsonResponseBody.put(JsonKeys.ID, FILE_ID);
        jsonResponseBody.put(JsonKeys.SOURCE, SOURCE);

        InputStream responseStream =
                new ByteArrayInputStream(jsonResponseBody.toString().getBytes());
        MockHttpEntity responseEntity = new MockHttpEntity(responseStream);
        StatusLine created = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_CREATED, "");
        MockHttpResponse response = new MockHttpResponse(responseEntity, created);

        this.mockClient.addHttpResponse(response);
    }

    protected void loadUploadLocationResponseBody() throws Exception {
        /* create folder response */
        JSONObject folder = new JSONObject();
        folder.put(JsonKeys.UPLOAD_LOCATION, "https://upload.location.com/some/path");

        InputStream uploadLocationStream =
                new ByteArrayInputStream(folder.toString().getBytes());
        MockHttpEntity uploadLocationEntity = new MockHttpEntity(uploadLocationStream);
        StatusLine ok = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "");
        MockHttpResponse uploadLocationResponse = new MockHttpResponse(uploadLocationEntity, ok);
        this.mockClient.setHttpResponse(uploadLocationResponse);
    }

    @Override
    protected String getMethod() {
        return HttpPut.METHOD_NAME;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // some upload requests perform two http requests, so clear any responses that may have
        // been entered already. each test here is responsible for loading 1 or two responses.
        this.mockClient.clearHttpResponseQueue();
        this.responseQueue = new LinkedBlockingQueue<LiveOperation>();
        this.queueingListener = new UploadOperationQueueingListener(this.exceptionQueue,
                                                                    this.responseQueue);
    }
}
