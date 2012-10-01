package com.microsoft.live;


import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.test.mock.MockApplication;

/**
 * This class has access to default (i.e., internal) classes and methods inside of com.microsoft.live.
 * It is used to assist test cases.
 */
public final class TestUtils {

    public static LiveConnectClient newLiveConnectClient(HttpClient client) {
        LiveAuthClient authClient = new LiveAuthClient(new MockApplication() {
            @Override
            public Context getApplicationContext() {
                return this;
            }
        }, "someclientid");

        LiveConnectSession session = new LiveConnectSession(authClient);
        session.setAccessToken("access_token");
        session.setAuthenticationToken("authentication_token");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3600);
        session.setExpiresIn(calendar.getTime());

        String[] scopes = {"scope"};
        session.setScopes(Arrays.asList(scopes));
        session.setRefreshToken("refresh_token");
        session.setTokenType("token_type");

        LiveConnectClient liveClient = new LiveConnectClient(session);
        liveClient.setHttpClient(client);

        return liveClient;
    }

    private TestUtils() { throw new AssertionError(); }
}
