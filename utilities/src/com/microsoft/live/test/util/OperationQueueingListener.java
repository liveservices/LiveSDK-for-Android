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
import com.microsoft.live.LiveOperationListener;

public class OperationQueueingListener extends QueueingListener<LiveOperation>
                                       implements LiveOperationListener {

	public OperationQueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
	                                 BlockingQueue<LiveOperation> responseQueue) {
	    super(exceptionQueue, responseQueue);
	}

    @Override
    public void onComplete(LiveOperation operation) {
        this.responseQueue.add(operation);
    }

    @Override
    public void onError(LiveOperationException exception,
                        LiveOperation operation) {
        this.exceptionQueue.add(exception);
        this.responseQueue.add(operation);
    }
}