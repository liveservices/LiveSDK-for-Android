package com.microsoft.live.unittest;

import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import android.text.TextUtils;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.test.util.MoveAsyncRunnable;

public class MoveTest extends FileOperationApiTest {

    private static final String KEY = "key";

    /** HTTP method this class is testing */
    private static final String METHOD = "MOVE";

    private static final String VALUE = "value";

    @Override
    public void testSyncResponseBodyInvalid() throws Exception {
        this.loadInvalidResponseBody();

        try {
            String requestPath = "folder.12319";
            String destination = "folder.1239081";
            this.liveConnectClient.move(requestPath, destination);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertFalse(TextUtils.isEmpty(e.getMessage()));
        }
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadValidResponseBody();

        String requestPath = "folder.181231";
        String destination = "folder.1231";
        LiveOperation operation = this.liveConnectClient.move(requestPath, destination);

        this.checkOperationMembers(operation, METHOD, requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) throws Exception {
        JSONObject result = operation.getResult();
        assertEquals(VALUE, result.getString(KEY));
    }

    @Override
    protected MoveAsyncRunnable createAsyncRunnable(String requestPath,
                                                    String destination) {
        return new MoveAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     destination,
                                     this.queueingListener);
    }

    @Override
    protected MoveAsyncRunnable createAsyncRunnable(String requestPath,
                                                    String destination,
                                                    Object userState) {
        return new MoveAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     destination,
                                     this.queueingListener,
                                     userState);
    }

    @Override
    protected String getMethod() {
        return METHOD;
    }

    @Override
    protected void loadValidResponseBody() throws Exception {
        JSONObject responseBody = new JSONObject();
        responseBody.put(KEY, VALUE);
        this.mockEntity.setInputStream(new ByteArrayInputStream(responseBody.toString().getBytes()));
    }
}