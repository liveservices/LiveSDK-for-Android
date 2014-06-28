//------------------------------------------------------------------------------
// Copyright 2014 Microsoft Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//------------------------------------------------------------------------------

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