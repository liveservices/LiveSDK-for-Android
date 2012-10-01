package com.microsoft.live.unittest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.test.InstrumentationTestCase;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.TestUtils;
import com.microsoft.live.constants.ErrorCodes;
import com.microsoft.live.constants.ErrorMessages;
import com.microsoft.live.constants.JsonKeys;
import com.microsoft.live.mock.MockHttpClient;
import com.microsoft.live.mock.MockHttpEntity;
import com.microsoft.live.mock.MockHttpResponse;
import com.microsoft.live.test.util.AssertMessages;
import com.microsoft.live.test.util.AsyncRunnable;

public abstract class ApiTest<OperationType, ListenerType> extends InstrumentationTestCase {

    private static final String INVALID_FORMAT = "!@#098 {} [] This is an invalid formated " +
                                                 "response body asdfkaj{}dfa(*&!@#";

    /**
     * Changes the mockClient to one that checks if the incoming requests contains the
     * Live Library header and then reverts to the original mockClient.
     */
    protected void loadLiveLibraryHeaderChecker() {
        final MockHttpClient currentMockClient = this.mockClient;
        MockHttpClient liveLibraryHeaderCheckerClient = new MockHttpClient() {
            @Override
            public HttpResponse execute(HttpUriRequest request) throws IOException,
                                                                       ClientProtocolException {
                Header header = request.getFirstHeader("X-HTTP-Live-Library");

                assertEquals("android/" + Build.VERSION.RELEASE + "_5.0", header.getValue());

                // load the old mock client back after we check.
                mockClient = currentMockClient;
                return currentMockClient.execute(request);
            }

            @Override
            public HttpResponse getHttpResponse() {
                return currentMockClient.getHttpResponse();
            }

            @Override
            public void setHttpResponse(HttpResponse httpResponse) {
                currentMockClient.setHttpResponse(httpResponse);
            }

            @Override
            public void addHttpResponse(HttpResponse httpResponse) {
                currentMockClient.addHttpResponse(httpResponse);
            }

            @Override
            public void clearHttpResponseQueue() {
                currentMockClient.clearHttpResponseQueue();
            }
        };

        this.mockClient = liveLibraryHeaderCheckerClient;
    }

    /** wait time to retrieve a response from a blocking queue for an async call*/
    protected static int WAIT_TIME_IN_SECS = 10;

    protected static void assertEquals(InputStream is, String response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            // Close silently.
            // Close could throw an exception, and if we don't catch it, it will trump
            // the originally thrown exception.
            // Unfortunately, this means that if there was a problem only with close the reader,
            // it will be ignored. I assume we can safely ignore this case, because if there is
            // a problem closing a stream, there is little we can do to correc this.
            try { reader.close(); } catch (Exception e) { }
        }

        assertEquals(response, sb.toString());
    }

	protected BlockingQueue<LiveOperationException> exceptionQueue;
	protected LiveConnectClient liveConnectClient;
	protected MockHttpClient mockClient;
	protected MockHttpEntity mockEntity;
	protected MockHttpResponse mockResponse;
	protected ListenerType queueingListener;
	protected BlockingQueue<OperationType> responseQueue;

	public abstract void testAsyncResponseBodyInvalid() throws Throwable;
	public abstract void testAsyncResponseBodyValid() throws Throwable;
	public abstract void testAsyncResponseBodyValidWithUserState() throws Throwable;
	public abstract void testSyncResponseBodyInvalid() throws Exception;
	public abstract void testSyncResponseBodyValid() throws Exception;

    protected abstract void checkValidResponseBody(OperationType operation) throws Exception;
    protected abstract AsyncRunnable<OperationType, ListenerType> createAsyncRunnable(String requestPath);
    protected abstract AsyncRunnable<OperationType, ListenerType> createAsyncRunnable(String requestPath,
                                                                                      Object userState);
    protected abstract void loadValidResponseBody() throws Exception;
    protected abstract String getMethod();

    protected void checkOperationMembers(LiveDownloadOperation operation,
                                         String method,
                                         String path) {
        this.checkOperationMembers(operation, method, path, null);
    }


	protected void checkOperationMembers(LiveDownloadOperation operation,
	                                     String method,
	                                     String path,
	                                     Object userState) {
        assertEquals(method, operation.getMethod());
        assertEquals(path, operation.getPath());
        assertEquals(userState, operation.getUserState());
    }

    /**
	 * Asserts that the given LiveOperation has the correct method, path, and userState
	 *
	 * @param operation
	 * @param method
	 * @param path
	 */
	protected void checkOperationMembers(LiveOperation operation,
	                                     String method,
	                                     String path) {
	    this.checkOperationMembers(operation, method, path, null);
	}

	/**
	 * Asserts that the given LiveOperation has the correct method, path, and userState.
	 *
	 * @param operation
	 * @param method
	 * @param path
	 * @param userState
	 */
	protected void checkOperationMembers(LiveOperation operation,
                                         String method,
                                         String path,
                                         Object userState) {
        assertEquals(method, operation.getMethod());
        assertEquals(path, operation.getPath());
        assertEquals(userState, operation.getUserState());
    }

	/**
	 * Asserts the response contains the error for an Invalid Path.
	 *
	 * @param operation
	 * @param requestPath
	 * @throws JSONException
	 */
	protected void checkPathInvalid(LiveOperation operation,
	                                String requestPath) throws JSONException {
        JSONObject result = operation.getResult();
        JSONObject error = result.getJSONObject(JsonKeys.ERROR);
        String message = error.getString(JsonKeys.MESSAGE);
        String code = error.getString(JsonKeys.CODE);

        assertEquals(String.format(ErrorMessages.URL_NOT_VALID, requestPath.toLowerCase()),
                     message);
        assertEquals(ErrorCodes.REQUEST_URL_INVALID, code);

        String rawResult = operation.getRawResult();
        assertEquals(result.toString(), rawResult);
    }

	protected void checkResponseBodyInvalid(LiveDownloadOperation operation) throws IOException {
	    InputStream is = operation.getStream();
	    ApiTest.assertEquals(is, INVALID_FORMAT);
	}

	/**
	 * Asserts the LiveOperation's result and raw result members are null.
	 *
	 * @param operation
	 */
	protected void checkResponseBodyInvalid(LiveOperation operation) {
        JSONObject result = operation.getResult();
        assertNull(result);

        String rawResult = operation.getRawResult();
        assertNull(rawResult);
    }

    protected void checkReturnedException(LiveDownloadOperation fromMethod,
	                                      LiveDownloadOperation fromCallback,
	                                      LiveOperationException exception) throws IOException {
        assertNotNull(exception.getMessage());

        this.checkReturnedOperations(fromMethod, fromCallback);
	}

	/**
     * Asserts the returned exception is not null and the LiveOperations from the method,
     * and the callback listener are the same object. Also, asserts the responseQueue,
     * and exceptionQueue are empty.
     *
     * @param fromMethod
     * @param fromCallback
     * @param exception
     */
    protected void checkReturnedException(LiveOperation fromMethod,
	                                      LiveOperation fromCallback,
	                                      LiveOperationException exception) {
        assertNotNull(exception.getMessage());

        this.checkReturnedOperations(fromMethod, fromCallback);

        this.checkResponseBodyInvalid(fromMethod);
	}

	/**
     * Asserts the returned LiveOperations from the method, and from the callback listener
     * are the same object. Also, asserts the responseQueue and exceptionQueue are empty.
     *
     * @param fromMethod
     * @param fromCallback
     */
    protected <T> void checkReturnedOperations(T fromMethod, T fromCallback) {
        assertTrue(fromMethod == fromCallback);
        assertTrue(this.responseQueue.isEmpty());
        assertTrue(this.exceptionQueue.isEmpty());
	}

    protected void failNoIllegalArgumentExceptionThrown() {
        this.failNoExceptionThrown(IllegalArgumentException.class);
    }

    protected void failNoLiveOperationExceptionThrown() {
        this.failNoExceptionThrown(LiveOperationException.class);
    }

    protected void failNoNullPointerExceptionThrown() {
        this.failNoExceptionThrown(NullPointerException.class);
	}

    /** Loads an invalid formated body into the HttpClient
     * @throws Exception */
    protected void loadInvalidResponseBody() throws Exception {
        byte[] bytes = INVALID_FORMAT.getBytes();
        this.mockResponse.addHeader(HTTP.CONTENT_LEN, Long.toString(bytes.length));
        this.mockEntity.setInputStream(new ByteArrayInputStream(bytes));
        this.mockClient.setHttpResponse(mockResponse);
    }

    /**
     * Loads an invalid path response into the HttpClient.
     *
     * @param requestPath
     * @throws Exception
     */
    protected void loadPathInvalidResponse(String requestPath) throws Exception {
	    JSONObject error = new JSONObject();
	    error.put(JsonKeys.CODE, ErrorCodes.REQUEST_URL_INVALID);

	    String message = String.format(ErrorMessages.URL_NOT_VALID,
	                                         requestPath.toLowerCase());
	    error.put(JsonKeys.MESSAGE, message);

	    JSONObject response = new JSONObject();
	    response.put(JsonKeys.ERROR, error);

	    byte[] bytes = response.toString().getBytes();
        this.mockResponse.addHeader(HTTP.CONTENT_LEN, Long.toString(bytes.length));
	    this.mockEntity.setInputStream(new ByteArrayInputStream(bytes));
	    StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1,
	                                            HttpStatus.SC_BAD_REQUEST,
	                                            "Bad Request");
	    this.mockResponse.setStatusLine(status);
	    this.mockClient.setHttpResponse(this.mockResponse);
	}

    protected LiveOperationException pollExceptionQueue() throws InterruptedException {
        return this.exceptionQueue.poll(WAIT_TIME_IN_SECS, TimeUnit.SECONDS);
    }

    protected OperationType pollResponseQueue() throws InterruptedException {
        return this.responseQueue.poll(WAIT_TIME_IN_SECS, TimeUnit.SECONDS);
    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Set up the MockClient
		this.mockEntity = new MockHttpEntity();

		StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1,
		                                            HttpStatus.SC_OK,
		                                            "OK");
    	this.mockResponse = new MockHttpResponse(this.mockEntity, statusLine);
    	this.mockClient = new MockHttpClient(this.mockResponse);
        this.loadLiveLibraryHeaderChecker();
    	this.exceptionQueue = new LinkedBlockingQueue<LiveOperationException>();
    	this.liveConnectClient = TestUtils.newLiveConnectClient(this.mockClient);
	}

	@Override
	protected void tearDown() throws Exception {
	    super.tearDown();

	    this.mockEntity = null;
	    this.mockClient = null;
	    this.responseQueue = null;
	    this.exceptionQueue = null;
	    this.queueingListener = null;
	    this.liveConnectClient = null;
	}

	private void failNoExceptionThrown(Class<?> cl) {
        fail(String.format(AssertMessages.NO_EXCEPTION_THROWN, cl));
    }
}
