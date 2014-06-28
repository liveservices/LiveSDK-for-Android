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
