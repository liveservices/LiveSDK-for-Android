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

package com.microsoft.live;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.microsoft.live.EntityEnclosingApiRequest.UploadProgressListener;

/**
 * ApiRequestAsync performs an async ApiRequest by subclassing AsyncTask
 * and executing the request inside of doInBackground and giving the
 * response to the appropriate listener on the main/UI thread.
 */
class ApiRequestAsync<ResponseType> extends AsyncTask<Void, Long, Runnable>
                                    implements UploadProgressListener {

    public interface Observer<ResponseType> {
        public void onComplete(ResponseType result);

        public void onError(LiveOperationException e);
    }

    public interface ProgressObserver {
        public void onProgress(Long... values);
    }

    private class OnCompleteRunnable implements Runnable {

        private final ResponseType response;

        public OnCompleteRunnable(ResponseType response) {
            assert response != null;

            this.response = response;
        }

        @Override
        public void run() {
            for (Observer<ResponseType> observer : observers) {
                observer.onComplete(this.response);
            }
        }
    }

    private class OnErrorRunnable implements Runnable {

        private final LiveOperationException exception;

        public OnErrorRunnable(LiveOperationException exception) {
            assert exception != null;

            this.exception = exception;
        }

        @Override
        public void run() {
            for (Observer<ResponseType> observer : observers) {
                observer.onError(this.exception);
            }
        }
    }

    /**
     * Static constructor. Prefer to use this over the normal constructor, because
     * this will infer the generic types, and be less verbose.
     *
     * @param request
     * @return a new ApiRequestAsync
     */
    public static <T> ApiRequestAsync<T> newInstance(ApiRequest<T> request) {
        return new ApiRequestAsync<T>(request);
    }

    /**
     * Static constructor. Prefer to use this over the normal constructor, because
     * this will infer the generic types, and be less verbose.
     *
     * @param request
     * @return a new ApiRequestAsync
     */
    public static <T> ApiRequestAsync<T> newInstance(EntityEnclosingApiRequest<T> request) {
        return new ApiRequestAsync<T>(request);
    }

    private final ArrayList<Observer<ResponseType>> observers;
    private final ArrayList<ProgressObserver> progressListeners;
    private final ApiRequest<ResponseType> request;

    {
        this.observers = new ArrayList<Observer<ResponseType>>();
        this.progressListeners = new ArrayList<ProgressObserver>();
    }

    /**
     * Constructs a new ApiRequestAsync object and initializes its member variables.
     *
     * This method attaches a progress observer to the EntityEnclosingApiRequest, and call
     * publicProgress when ever there is an on progress event.
     *
     * @param request
     */
    public ApiRequestAsync(EntityEnclosingApiRequest<ResponseType> request) {
        assert request != null;

        // Whenever the request has upload progress we need to publish the progress, so
        // listen to progress events.
        request.addListener(this);

        this.request = request;
    }

    /**
     * Constructs a new ApiRequestAsync object and initializes its member variables.
     *
     * @param operation to launch in an asynchronous manner
     */
    public ApiRequestAsync(ApiRequest<ResponseType> request) {
        assert request != null;

        this.request = request;
    }

    public boolean addObserver(Observer<ResponseType> observer) {
        return this.observers.add(observer);
    }

    public boolean addProgressObserver(ProgressObserver observer) {
        return this.progressListeners.add(observer);
    }

    @Override
    public void onProgress(long totalBytes, long numBytesWritten) {
        publishProgress(Long.valueOf(totalBytes), Long.valueOf(numBytesWritten));
    }

    public boolean removeObserver(Observer<ResponseType> observer) {
        return this.observers.remove(observer);
    }

    public boolean removeProgressObserver(ProgressObserver observer) {
        return this.progressListeners.remove(observer);
    }

    @Override
    protected Runnable doInBackground(Void... args) {
        ResponseType response;

        try {
            response = this.request.execute();
        } catch (LiveOperationException e) {
            return new OnErrorRunnable(e);
        }

        return new OnCompleteRunnable(response);
    }

    @Override
    protected void onPostExecute(Runnable result) {
        super.onPostExecute(result);
        result.run();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        for (ProgressObserver listener : this.progressListeners) {
            listener.onProgress(values);
        }
    }
}
