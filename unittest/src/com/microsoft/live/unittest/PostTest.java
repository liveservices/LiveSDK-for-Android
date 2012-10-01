package com.microsoft.live.unittest;


import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.constants.JsonKeys;
import com.microsoft.live.constants.Paths;
import com.microsoft.live.test.util.PostAsyncRunnable;


public class PostTest extends JsonEnclosingApiTest {

    /** The body of the post request. */
    private static final JSONObject CALENDAR;

    private static final String METHOD = "POST";

    /** Name of the calendar to be created */
    private static final String NAME = "Test Calendar";

    static {
        Map<String, String> calendar = new HashMap<String, String>();
        calendar.put(JsonKeys.NAME, NAME);

        CALENDAR = new JSONObject(calendar);
    }


    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = Paths.INVALID;

        this.runTestOnUiThread(createAsyncRunnable(requestPath, CALENDAR));

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

        String requestPath = Paths.ME_CALENDARS;

        this.runTestOnUiThread(createAsyncRunnable(requestPath, CALENDAR));

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
        String requestPath = Paths.ME_CALENDARS;

        this.runTestOnUiThread(createAsyncRunnable(requestPath, CALENDAR, userState));

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
            this.liveConnectClient.post(Paths.ME_CALENDARS, CALENDAR);
            this.failNoLiveOperationExceptionThrown();
        } catch (LiveOperationException e) {
            assertFalse(TextUtils.isEmpty(e.getMessage()));
        }
    }

    @Override
    public void testSyncResponseBodyValid() throws Exception {
        this.loadValidResponseBody();

        String requestPath = Paths.ME_CALENDARS;

        LiveOperation operation = this.liveConnectClient.post(requestPath, CALENDAR);

        this.checkOperationMembers(operation, METHOD, requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) throws JSONException {
        JSONObject result = operation.getResult();
        String id = result.getString(JsonKeys.ID);
        Object description = result.get(JsonKeys.DESCRIPTION);
        String name = result.getString(JsonKeys.NAME);
        String permissions = result.getString(JsonKeys.PERMISSIONS);
        boolean isDefault = result.getBoolean(JsonKeys.IS_DEFAULT);

        JSONObject from = result.getJSONObject(JsonKeys.FROM);
        String fromId = from.getString(JsonKeys.ID);
        String fromName = from.getString(JsonKeys.NAME);

        Object subscriptionLocation = result.get(JsonKeys.SUBSCRIPTION_LOCATION);
        String createdTime = result.getString(JsonKeys.CREATED_TIME);
        String updatedTime = result.getString(JsonKeys.UPDATED_TIME);

        assertEquals("calendar_id", id);
        assertEquals(JSONObject.NULL, description);
        assertEquals("name", name);
        assertEquals("owner", permissions);
        assertEquals(false, isDefault);
        assertEquals("from_id", fromId);
        assertEquals("from_name", fromName);
        assertEquals(JSONObject.NULL, subscriptionLocation);
        assertEquals("2011-12-10T02:48:33+0000", createdTime);
        assertEquals("2011-12-10T02:48:33+0000", updatedTime);
    }

    @Override
    protected PostAsyncRunnable createAsyncRunnable(String requestPath, JSONObject body) {
        return new PostAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     body,
                                     this.queueingListener);
    }

    @Override
    protected PostAsyncRunnable createAsyncRunnable(String requestPath,
                                                    JSONObject body,
                                                    Object userState) {
        return new PostAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     body,
                                     this.queueingListener,
                                     userState);
    }

    @Override
    protected PostAsyncRunnable createAsyncRunnable(String requestPath, String body) {
        return new PostAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     body,
                                     this.queueingListener);
    }

    @Override
    protected PostAsyncRunnable createAsyncRunnable(String requestPath,
                                                    String body,
                                                    Object userState) {
        return new PostAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     body,
                                     this.queueingListener,
                                     userState);
    }

    @Override
    protected String getMethod() {
        return METHOD;
    }

    @Override
    protected void loadValidResponseBody() throws JSONException {
        JSONObject calendar = new JSONObject();
        calendar.put(JsonKeys.ID, "calendar_id");
        calendar.put(JsonKeys.DESCRIPTION, JSONObject.NULL);
        calendar.put(JsonKeys.NAME, "name");
        calendar.put(JsonKeys.PERMISSIONS, "owner");
        calendar.put(JsonKeys.IS_DEFAULT, false);

        JSONObject from = new JSONObject();
        from.put(JsonKeys.ID, "from_id");
        from.put(JsonKeys.NAME, "from_name");

        calendar.put(JsonKeys.FROM, from);
        calendar.put(JsonKeys.SUBSCRIPTION_LOCATION, JSONObject.NULL);
        calendar.put(JsonKeys.CREATED_TIME, "2011-12-10T02:48:33+0000");
        calendar.put(JsonKeys.UPDATED_TIME, "2011-12-10T02:48:33+0000");

        byte[] bytes = calendar.toString().getBytes();
        this.mockEntity.setInputStream(new ByteArrayInputStream(bytes));
    }
}
