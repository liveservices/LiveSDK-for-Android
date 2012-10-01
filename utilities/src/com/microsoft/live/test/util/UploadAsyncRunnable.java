package com.microsoft.live.test.util;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveUploadOperationListener;
import com.microsoft.live.OverwriteOption;

public class UploadAsyncRunnable extends AsyncRunnable<LiveOperation, LiveUploadOperationListener> {

    private final String filename;
    private final OverwriteOption overwrite;
    private final InputStream is;
    private final File file;

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               InputStream is,
                               LiveUploadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = null;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               InputStream is,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = null;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               OverwriteOption overwrite,
                               InputStream is,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = overwrite;
        this.is = is;
        this.file = null;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               File file,
                               LiveUploadOperationListener listener) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = null;
        this.is = null;
        this.file = file;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               File file,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.filename = filename;
        this.overwrite = null;
        this.is = null;
        this.file = file;
    }

    public UploadAsyncRunnable(BlockingQueue<LiveOperation> queue,
                               LiveConnectClient connectClient,
                               String path,
                               String filename,
                               OverwriteOption overwrite,
                               File file,
                               LiveUploadOperationListener listener,
                               Object userState) {
        super(queue, connectClient, path, listener);
        this.filename = filename;
        this.overwrite = overwrite;
        this.is = null;
        this.file = file;
    }

    @Override
    protected LiveOperation calledWithoutUserState() {
        if (this.file == null) {
            return this.connectClient.uploadAsync(this.path, this.filename, this.is, this.listener);
        } else {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.file,
                                                  this.listener);
        }
    }

    @Override
    protected LiveOperation calledWithUserState(Object userState) {
        boolean hasFile = this.file != null;
        if (this.overwrite == null) {
            if (hasFile) {
                return this.connectClient.uploadAsync(this.path,
                                                      this.filename,
                                                      this.file,
                                                      this.listener,
                                                      userState);
            } else {
                return this.connectClient.uploadAsync(this.path,
                                                      this.filename,
                                                      this.is,
                                                      this.listener,
                                                      userState);
            }
        }

        if (hasFile) {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.file,
                                                  this.overwrite,
                                                  this.listener,
                                                  userState);
        } else {
            return this.connectClient.uploadAsync(this.path,
                                                  this.filename,
                                                  this.is,
                                                  this.overwrite,
                                                  this.listener,
                                                  userState);
        }
    }
}
