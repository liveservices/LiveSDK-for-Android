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

import java.io.File;
import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;

public class DownloadAsyncRunnable extends AsyncRunnable<LiveDownloadOperation,
                                                         LiveDownloadOperationListener> {
    private final File file;

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 LiveDownloadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.file = null;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 LiveDownloadOperationListener listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.file = null;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 File file,
                                 LiveDownloadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.file = file;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 File file,
                                 LiveDownloadOperationListener listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.file = file;
    }

    @Override
    protected LiveDownloadOperation calledWithoutUserState() {
        if (this.file != null) {
            return connectClient.downloadAsync(path, file, listener);
        }
        return this.connectClient.downloadAsync(this.path, this.listener);
    }

    @Override
    protected LiveDownloadOperation calledWithUserState(Object userState) {
        if (this.file != null) {
            return connectClient.downloadAsync(path, file, listener, userState);
        }
        return this.connectClient.downloadAsync(this.path, this.listener, userState);
    }

}
