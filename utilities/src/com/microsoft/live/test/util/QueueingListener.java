package com.microsoft.live.test.util;

import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveOperationException;

public abstract class QueueingListener<OperationType> {
	final protected BlockingQueue<LiveOperationException> exceptionQueue;
	final protected BlockingQueue<OperationType> responseQueue;

	public QueueingListener(BlockingQueue<LiveOperationException> exceptionQueue,
	                        BlockingQueue<OperationType> responseQueue) {
	    this.exceptionQueue = exceptionQueue;
	    this.responseQueue = responseQueue;
	}
}
