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
