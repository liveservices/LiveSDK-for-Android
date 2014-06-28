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
