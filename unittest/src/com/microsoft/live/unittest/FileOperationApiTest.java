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

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.test.util.AsyncRunnable;
import com.microsoft.live.test.util.AsyncRunnableWithDestination;
import com.microsoft.live.test.util.OperationQueueingListener;

public abstract class FileOperationApiTest extends ApiTest<LiveOperation, LiveOperationListener> {

    @Override
    public void testAsyncResponseBodyInvalid() throws Throwable {
        this.loadInvalidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();
        LiveOperationException exception = this.pollExceptionQueue();

        this.checkReturnedException(fromMethod, fromCallback, exception);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkResponseBodyInvalid(fromMethod);

    }

    @Override
    public void testAsyncResponseBodyValid() throws Throwable {
        this.loadValidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath);
        this.checkValidResponseBody(fromMethod);
    }

    @Override
    public void testAsyncResponseBodyValidWithUserState() throws Throwable {
        this.loadValidResponseBody();

        String requestPath = "file.123123";
        String destination = "file.123109";
        Object userState = new Object();

        this.runTestOnUiThread(createAsyncRunnable(requestPath, destination, userState));

        LiveOperation fromMethod = this.responseQueue.take();
        LiveOperation fromCallback = this.pollResponseQueue();

        this.checkReturnedOperations(fromMethod, fromCallback);
        this.checkOperationMembers(fromMethod, this.getMethod(), requestPath, userState);
        this.checkValidResponseBody(fromMethod);
    }

    protected abstract AsyncRunnableWithDestination<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String destination);

    protected abstract AsyncRunnableWithDestination<LiveOperation, LiveOperationListener>
            createAsyncRunnable(String requestPath, String destination, Object userState);

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
