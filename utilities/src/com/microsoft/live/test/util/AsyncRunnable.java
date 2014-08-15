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
