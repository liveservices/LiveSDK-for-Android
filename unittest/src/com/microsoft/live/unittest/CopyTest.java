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

package com.microsoft.live.unittest;

import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import android.text.TextUtils;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.test.util.CopyAsyncRunnable;

public class CopyTest extends FileOperationApiTest {

    private static final String KEY = "key";

    /** HTTP method this class is testing */
    private static final String METHOD = "COPY";

    private static final String VALUE = "value";

    @Override
    public void testSyncResponseBodyInvalid() throws Exception {
        this.loadInvalidResponseBody();

        try {
            String requestPath = "folder.12319";
            String destination = "folder.1239081";
            this.liveConnectClient.copy(requestPath, destination);
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
        LiveOperation operation = this.liveConnectClient.copy(requestPath, destination);

        this.checkOperationMembers(operation, METHOD, requestPath);
        this.checkValidResponseBody(operation);
    }

    @Override
    protected void checkValidResponseBody(LiveOperation operation) throws Exception {
        JSONObject result = operation.getResult();
        assertEquals(VALUE, result.getString(KEY));
    }

    @Override
    protected CopyAsyncRunnable createAsyncRunnable(String requestPath,
                                                    String destination) {
        return new CopyAsyncRunnable(this.responseQueue,
                                     this.liveConnectClient,
                                     requestPath,
                                     destination,
                                     this.queueingListener);
    }

    @Override
    protected CopyAsyncRunnable createAsyncRunnable(String requestPath,
                                                    String destination,
                                                    Object userState) {
        return new CopyAsyncRunnable(this.responseQueue,
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
