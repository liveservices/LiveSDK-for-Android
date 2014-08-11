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

import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;
import com.microsoft.live.LiveOperationException;

public class DownloadOperationQueueingListener extends QueueingListener<LiveDownloadOperation>
                                               implements LiveDownloadOperationListener {

    public DownloadOperationQueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
                                             BlockingQueue<LiveDownloadOperation> responseQueue) {
        super(exceptionQueue, responseQueue);
    }

    @Override
    public void onDownloadCompleted(LiveDownloadOperation operation) {
        this.responseQueue.add(operation);
    }

    @Override
    public void onDownloadFailed(LiveOperationException exception, LiveDownloadOperation operation) {
        this.exceptionQueue.add(exception);
        this.responseQueue.add(operation);
    }

    @Override
    public void onDownloadProgress(int totalBytes,
                                   int bytesRemaining,
                                   LiveDownloadOperation operation) {
        // TODO(skrueger): add support for progress
    }
}
