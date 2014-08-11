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
