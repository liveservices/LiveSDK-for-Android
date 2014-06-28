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

import org.json.JSONObject;

import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationListener;

public class PostAsyncRunnable extends AsyncRunnableWithBody<LiveOperation, LiveOperationListener> {

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             JSONObject body,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, body, listener);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             JSONObject body,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, body, listener, userState);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String body,
                             LiveOperationListener listener) {
        super(queue, connectClient, path, body, listener);
    }

    public PostAsyncRunnable(BlockingQueue<LiveOperation> queue,
                             LiveConnectClient connectClient,
                             String path,
                             String body,
                             LiveOperationListener listener,
                             Object userState) {
        super(queue, connectClient, path, body, listener, userState);
    }

    @Override
    protected LiveOperation calledWithoutUserState(JSONObject body) {
        return connectClient.postAsync(path, body, listener);
    }

    @Override
    protected LiveOperation calledWithoutUserState(String body) {
        return connectClient.postAsync(path, body, listener);
    }

    @Override
    protected LiveOperation calledWithUserState(JSONObject body, Object userState) {
        return connectClient.postAsync(path, body, listener, userState);
    }

    @Override
    protected LiveOperation calledWithUserState(String body, Object userState) {
        return connectClient.postAsync(path, body, listener, userState);
    }

}
