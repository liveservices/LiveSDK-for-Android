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

public abstract class AsyncRunnableWithDestination<OperationType, ListenerType>
        extends AsyncRunnable<OperationType, ListenerType> {

    protected final String destination;

    public AsyncRunnableWithDestination(BlockingQueue<OperationType> queue,
                                        LiveConnectClient connectClient,
                                        String path,
                                        String destination,
                                        ListenerType listener) {
        super(queue, connectClient, path, listener);
        this.destination = destination;
    }

    public AsyncRunnableWithDestination(BlockingQueue<OperationType> queue,
                                        LiveConnectClient connectClient,
                                        String path,
                                        String destination,
                                        ListenerType listener,
                                        Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.destination = destination;
    }

}
