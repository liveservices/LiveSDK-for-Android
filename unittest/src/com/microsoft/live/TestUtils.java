// ------------------------------------------------------------------------------
// Copyright (c) 2014 Microsoft Corporation
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ------------------------------------------------------------------------------

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

    public static LiveAuthClient newMockLiveAuthClient() {
        return new LiveAuthClient(new MockApplication() {
            @Override
            public Context getApplicationContext() {
                return this;
            }
        }, "someclientid");
    }

    public static LiveConnectSession newMockLiveConnectSession() {
        LiveAuthClient authClient = TestUtils.newMockLiveAuthClient();
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

        return session;
    }

    public static LiveConnectClient newLiveConnectClient(HttpClient client) {
        LiveConnectSession session = TestUtils.newMockLiveConnectSession();
        LiveConnectClient liveClient = new LiveConnectClient(session);
        liveClient.setHttpClient(client);

        return liveClient;
    }

    private TestUtils() { throw new AssertionError(); }
}
