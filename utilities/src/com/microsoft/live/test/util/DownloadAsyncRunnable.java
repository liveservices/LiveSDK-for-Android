package com.microsoft.live.test.util;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveDownloadOperation;
import com.microsoft.live.LiveDownloadOperationListener;

public class DownloadAsyncRunnable extends AsyncRunnable<LiveDownloadOperation,
                                                         LiveDownloadOperationListener> {
    private final File file;

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 LiveDownloadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.file = null;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 LiveDownloadOperationListener listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.file = null;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 File file,
                                 LiveDownloadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.file = file;
    }

    public DownloadAsyncRunnable(BlockingQueue<LiveDownloadOperation> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 File file,
                                 LiveDownloadOperationListener listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.file = file;
    }

    @Override
    protected LiveDownloadOperation calledWithoutUserState() {
        if (this.file != null) {
            return connectClient.downloadAsync(path, file, listener);
        }
        return this.connectClient.downloadAsync(this.path, this.listener);
    }

    @Override
    protected LiveDownloadOperation calledWithUserState(Object userState) {
        if (this.file != null) {
            return connectClient.downloadAsync(path, file, listener, userState);
        }
        return this.connectClient.downloadAsync(this.path, this.listener, userState);
    }

}
