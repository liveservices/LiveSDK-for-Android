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

public abstract class AsyncRunnable<OperationType, ListenerType> implements Runnable {

    private final boolean calledWithUserState;
    private final BlockingQueue<OperationType> queue;
    private final Object userState;
    protected final LiveConnectClient connectClient;
    protected final ListenerType listener;
    protected final String path;

	public AsyncRunnable(BlockingQueue<OperationType> queue,
	                     LiveConnectClient connectClient,
	                     String path,
	                     ListenerType listener) {
	    this.queue = queue;
	    this.connectClient = connectClient;
        this.listener = listener;
        this.path = path;
        this.userState = null;
        this.calledWithUserState = false;
	}

	public AsyncRunnable(BlockingQueue<OperationType> queue,
	                     LiveConnectClient connectClient,
	                     String path,
	                     ListenerType listener,
	                     Object userState) {
	    this.queue = queue;
	    this.connectClient = connectClient;
        this.listener = listener;
        this.path = path;
        this.userState = userState;
        this.calledWithUserState = true;
	}

	@Override
	public void run() {
	    if (calledWithUserState) {
	        queue.add(calledWithUserState(userState));
	    } else {
	        queue.add(calledWithoutUserState());
	    }
	}

	protected abstract OperationType calledWithoutUserState();
	protected abstract OperationType calledWithUserState(Object userState);
}
