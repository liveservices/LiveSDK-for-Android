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

import org.json.JSONObject;

import com.microsoft.live.LiveConnectClient;

public abstract class AsyncRunnableWithBody<OperationType, ListenerType>
        extends AsyncRunnable<OperationType, ListenerType> {

    private enum BodyType {
        JSON {
            @Override
            public <OperationType> OperationType calledWithoutUserState(AsyncRunnableWithBody<OperationType, ?> a) {
                return a.calledWithoutUserState(a.jsonBody);
            }

            @Override
            public <OperationType> OperationType calledWithUserState(AsyncRunnableWithBody<OperationType, ?> a,
                                                                     Object userState) {
                return a.calledWithUserState(a.jsonBody, userState);
            }
        },
        STRING {
            @Override
            public <OperationType> OperationType calledWithoutUserState(AsyncRunnableWithBody<OperationType, ?> a) {
                return a.calledWithoutUserState(a.stringBody);
            }

            @Override
            public <OperationType> OperationType calledWithUserState(AsyncRunnableWithBody<OperationType, ?> a,
                                                                     Object userState) {
                return a.calledWithUserState(a.stringBody, userState);
            }
        };

        public abstract <OperationType> OperationType calledWithoutUserState(AsyncRunnableWithBody<OperationType, ?> a);
        public abstract <OperationType> OperationType calledWithUserState(AsyncRunnableWithBody<OperationType, ?> a,
                                                                          Object userState);
    }

    private final BodyType bodyType;
    private final JSONObject jsonBody;
    private final String stringBody;

    public AsyncRunnableWithBody(BlockingQueue<OperationType> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 JSONObject body,
                                 ListenerType listener) {
        super(queue, connectClient, path, listener);
        this.jsonBody = body;
        this.stringBody = null;
        this.bodyType = BodyType.JSON;
    }

    public AsyncRunnableWithBody(BlockingQueue<OperationType> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 JSONObject body,
                                 ListenerType listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.jsonBody = body;
        this.stringBody = null;
        this.bodyType = BodyType.JSON;
    }

    public AsyncRunnableWithBody(BlockingQueue<OperationType> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 String body,
                                 ListenerType listener) {
        super(queue, connectClient, path, listener);
        this.jsonBody = null;
        this.stringBody = body;
        this.bodyType = BodyType.STRING;
    }

    public AsyncRunnableWithBody(BlockingQueue<OperationType> queue,
                                 LiveConnectClient connectClient,
                                 String path,
                                 String body,
                                 ListenerType listener,
                                 Object userState) {
        super(queue, connectClient, path, listener, userState);
        this.jsonBody = null;
        this.stringBody = body;
        this.bodyType = BodyType.STRING;
    }

    protected abstract OperationType calledWithoutUserState(JSONObject body);
    protected abstract OperationType calledWithoutUserState(String body);
    protected abstract OperationType calledWithUserState(JSONObject body, Object userState);
    protected abstract OperationType calledWithUserState(String body, Object userState);

    @Override
    protected OperationType calledWithoutUserState() {
        return this.bodyType.calledWithoutUserState(this);
    }

    @Override
    protected OperationType calledWithUserState(Object userState) {
        return this.bodyType.calledWithUserState(this, userState);
    }

}
