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

import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.test.util.AsyncRunnable;
import com.microsoft.live.test.util.AsyncRunnableWithBody;
import com.microsoft.live.test.util.OperationQueueingListener;

public abstract class JsonEnclosingApiTest extends ApiTest<LiveOperation, LiveOperationListener> {
    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, JSONObject body);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, JSONObject body, Object userState);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String body);

    protected abstract AsyncRunnableWithBody<LiveOperation, LiveOperationListener>
        createAsyncRunnable(String requestPath, String body, Object userState);

    @Override
    protected AsyncRunnable<LiveOperation, LiveOperationListener> createAsyncRunnable(String requestPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected AsyncRunnable<LiveOperation, LiveOperationListener> createAsyncRunnable(String requestPath,
                                                                                      Object userState) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.responseQueue = new LinkedBlockingQueue<LiveOperation>();
        this.queueingListener = new OperationQueueingListener(this.exceptionQueue,
                                                              this.responseQueue);
    }
}
