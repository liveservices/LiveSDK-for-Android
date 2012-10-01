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