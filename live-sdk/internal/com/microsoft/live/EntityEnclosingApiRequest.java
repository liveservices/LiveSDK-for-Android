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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * EntityEnclosingApiRequest is an ApiRequest with a body.
 * Upload progress can be monitored by adding an UploadProgressListener to this class.
 */
abstract class EntityEnclosingApiRequest<ResponseType> extends ApiRequest<ResponseType> {

    /**
     * UploadProgressListener is a listener that is called during upload progress.
     */
    public interface UploadProgressListener {

        /**
         * @param totalBytes of the upload request
         * @param numBytesWritten during the upload request
         */
        public void onProgress(long totalBytes, long numBytesWritten);
    }

    /**
     * Wraps the given entity, and intercepts writeTo calls to check the upload progress.
     */
    private static class ProgressableEntity extends HttpEntityWrapper {

        final List<UploadProgressListener> listeners;

        ProgressableEntity(HttpEntity wrapped, List<UploadProgressListener> listeners) {
            super(wrapped);

            assert listeners != null;
            this.listeners = listeners;
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            this.wrappedEntity.writeTo(new ProgressableOutputStream(outstream,
                                                                    this.getContentLength(),
                                                                    this.listeners));
            // If we don't consume the content, the content will be leaked (i.e., the InputStream
            // in the HttpEntity is not closed).
            // You'd think the library would call this.
            this.wrappedEntity.consumeContent();
        }
    }

    /**
     * Wraps the given output stream and notifies the given listeners, when the
     * stream is written to.
     */
    private static class ProgressableOutputStream extends FilterOutputStream {

        final List<UploadProgressListener> listeners;
        long numBytesWritten;
        long totalBytes;

        public ProgressableOutputStream(OutputStream outstream,
                                        long totalBytes,
                                        List<UploadProgressListener> listeners) {
            super(outstream);

            assert totalBytes >= 0L;
            assert listeners != null;

            this.listeners = listeners;
            this.numBytesWritten = 0L;
            this.totalBytes = totalBytes;
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            this.out.write(buffer);

            this.numBytesWritten += buffer.length;
            this.notifyListeners();
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.out.write(buffer, offset, count);

            this.numBytesWritten += count;
            this.notifyListeners();
        }

        @Override
        public void write(int oneByte) throws IOException {
            this.out.write(oneByte);

            this.numBytesWritten += 1;
            this.notifyListeners();
        }

        private void notifyListeners() {
            assert this.numBytesWritten <= this.totalBytes;

            for (final UploadProgressListener listener : this.listeners) {
                listener.onProgress(this.totalBytes, this.numBytesWritten);
            }
        }
    }

    protected final HttpEntity entity;

    private final List<UploadProgressListener> listeners;

    public EntityEnclosingApiRequest(LiveConnectSession session,
                                     HttpClient client,
                                     ResponseHandler<ResponseType> responseHandler,
                                     String path,
                                     HttpEntity entity) {
        this(session,
             client,
             responseHandler,
             path,
             entity,
             ResponseCodes.SUPPRESS,
             Redirects.SUPPRESS);
    }

    /**
     * Constructs a new EntiyEnclosingApiRequest and initializes its member variables.
     *
     * @param session that contains the access token
     * @param client to make Http Requests on
     * @param path of the request
     * @param entity of the request
     */
    public EntityEnclosingApiRequest(LiveConnectSession session,
                                     HttpClient client,
                                     ResponseHandler<ResponseType> responseHandler,
                                     String path,
                                     HttpEntity entity,
                                     ResponseCodes responseCodes,
                                     Redirects redirects) {
        super(session, client, responseHandler, path, responseCodes, redirects);

        assert entity != null;

        this.listeners = new ArrayList<UploadProgressListener>();
        this.entity = new ProgressableEntity(entity, this.listeners);
    }

    /**
     * Adds an UploadProgressListener to be called when there is upload progress.
     *
     * @param listener to add
     * @return always true
     */
    public boolean addListener(UploadProgressListener listener) {
        assert listener != null;

        return this.listeners.add(listener);
    }

    /**
     * Removes an UploadProgressListener.
     *
     * @param listener to be removed
     * @return true if the the listener was removed
     */
    public boolean removeListener(UploadProgressListener listener) {
        assert listener != null;

        return this.listeners.remove(listener);
    }
}
