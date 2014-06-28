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

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;


/**
 * This class is ran on the UI thread to prevent deadlocking.
 * It adds any operations or exceptions returned to the appropriate
 * blocking queue.
 */
public class CopyAsyncRunnable extends AsyncRunnableWithDestination<LiveOperation,
                                                                    LiveOperationListener> {

    public CopyAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, destination, listener);
    }

    public CopyAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String destination,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, destination, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        return connectClient.copyAsync(path, destination, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        return connectClient.copyAsync(path, destination, listener, userState);
    }

}
