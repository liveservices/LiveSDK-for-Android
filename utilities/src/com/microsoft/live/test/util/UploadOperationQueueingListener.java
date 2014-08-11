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

package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveUploadOperationListener;

public class UploadOperationQueueingListener extends QueueingListener<LiveOperation>
                                             implements LiveUploadOperationListener {

    public UploadOperationQueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
                                           BlockingQueue<LiveOperation> responseQueue) {
        super(exceptionQueue, responseQueue);
    }

    @Override
    public void onUploadCompleted(LiveOperation operation) {
        this.responseQueue.add(operation);
    }

    @Override
    public void onUploadFailed(LiveOperationException exception, LiveOperation operation) {
        this.exceptionQueue.add(exception);
        this.responseQueue.add(operation);
    }

    @Override
    public void onUploadProgress(int totalBytes, int bytesRemaining, LiveOperation operation) {
        // TODO(skrueger): add support for progress
    }
}
