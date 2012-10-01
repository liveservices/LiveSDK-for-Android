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
