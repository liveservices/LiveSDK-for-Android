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
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveUploadOperationListener;
import com.microsoft.live.OverwriteOption;

public class UploadAsyncRunnable extends AsyncRunnable<LiveOperation, LiveUploadOperationListener> {

    private final String filename;
    private final OverwriteOption overwrite;
    private final InputStream is;
    private final File file;

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               InputStream is,
                               LiveUploadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = null;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               InputStream is,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = null;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               OverwriteOption overwrite,
                               InputStream is,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = overwrite;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               File file,
                               LiveUploadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = null;
        this.is = null;
        this.file = file;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               File file,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = null;
        this.is = null;
        this.file = file;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               OverwriteOption overwrite,
                               File file,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = overwrite;
        this.is = null;
        this.file = file;
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        if (this.file == null) {
            return this.connectClient.uploadAsync(this.path, this.filename, this.is, this.listener);
        } else {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.file,
                                                  this.listener);
        }
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        boolean hasFile = this.file != null;
        if (this.overwrite == null) {
            if (hasFile) {
                return this.connectClient.uploadAsync(this.path,
                                                      this.filename,
                                                      this.file,
                                                      this.listener,
                                                      userState);
            } else {
                return this.connectClient.uploadAsync(this.path,
                                                      this.filename,
                                                      this.is,
                                                      this.listener,
                                                      userState);
            }
        }

        if (hasFile) {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.file,
                                                  this.overwrite,
                                                  this.listener,
                                                  userState);
        } else {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.is,
                                                  this.overwrite,
                                                  this.listener,
                                                  userState);
        }
    }
}
