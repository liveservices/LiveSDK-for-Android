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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

/**
 * ApiRequest is an abstract base class that represents an Http Request made by the API.
 * It does most of the Http Request work inside of the execute method, and provides a
 * an abstract factory method for subclasses to choose the type of Http Request to be
 * created.
 */
abstract class ApiRequest<ResponseType> {

    public interface Observer {
        public void onComplete(HttpResponse response);
    }

    public enum Redirects {
        SUPPRESS {
            @Override
            protected void setQueryParameterOn(UriBuilder builder) {
                Redirects.setQueryParameterOn(builder, Boolean.TRUE);
            }
        }, UNSUPPRESSED {
            @Override
            protected void setQueryParameterOn(UriBuilder builder) {
                Redirects.setQueryParameterOn(builder, Boolean.FALSE);
            }
        };

        /**
         * Sets the suppress_redirects query parameter by removing all existing ones
         * and then appending it on the given UriBuilder.
         */
        protected abstract void setQueryParameterOn(UriBuilder builder);

        private static void setQueryParameterOn(UriBuilder builder, Boolean value) {
            // The Live SDK is designed to use our value of suppress_redirects.
            // If it uses any other value it could cause issues. Remove any previously
            // existing suppress_redirects and use ours.
            builder.removeQueryParametersWithKey(QueryParameters.SUPPRESS_REDIRECTS);
            builder.appendQueryParameter(QueryParameters.SUPPRESS_REDIRECTS, value.toString());
        }
    }

    public enum ResponseCodes {
        SUPPRESS {
            @Override
            protected void setQueryParameterOn(UriBuilder builder) {
                ResponseCodes.setQueryParameterOn(builder, Boolean.TRUE);
            }
        }, UNSUPPRESSED {
            @Override
            protected void setQueryParameterOn(UriBuilder builder) {
                ResponseCodes.setQueryParameterOn(builder, Boolean.FALSE);
            }
        };

        /**
         * Sets the suppress_response_codes query parameter by removing all existing ones
         * and then appending it on the given UriBuilder.
         */
        protected abstract void setQueryParameterOn(UriBuilder builder);

        private static void setQueryParameterOn(UriBuilder builder, Boolean value) {
            // The Live SDK is designed to use our value of suppress_response_codes.
            // If it uses any other value it could cause issues. Remove any previously
            // existing suppress_response_codes and use ours.
            builder.removeQueryParametersWithKey(QueryParameters.SUPPRESS_RESPONSE_CODES);
            builder.appendQueryParameter(QueryParameters.SUPPRESS_RESPONSE_CODES, value.toString());
        }
    }

    private static final Header LIVE_LIBRARY_HEADER =
            new BasicHeader("X-HTTP-Live-Library", "android/" + Build.VERSION.RELEASE + "_" +
                                                   Config.INSTANCE.getApiVersion());
    private static final int SESSION_REFRESH_BUFFER_SECS = 30;
    private static final int SESSION_TOKEN_SEND_BUFFER_SECS = 3;

    /**
     * Constructs a new instance of a Header that contains the
     * @param accessToken to construct inside the Authorization header
     * @return a new instance of a Header that contains the Authorization access_token
     */
    private static Header createAuthroizationHeader(LiveConnectSession session) {
        assert session != null;

        String accessToken = session.getAccessToken();
        assert !TextUtils.isEmpty(accessToken);

        String tokenType = OAuth.TokenType.BEARER.toString().toLowerCase(Locale.US);
        String value = TextUtils.join(" ", new String[]{tokenType, accessToken});
        return new BasicHeader(AUTH.WWW_AUTH_RESP, value);
    }

    private final HttpClient client;
    private final List<Observer> observers;
    private final String path;
    private final ResponseHandler<ResponseType> responseHandler;
    private final LiveConnectSession session;

    protected final UriBuilder requestUri;

    /** The original path string parsed into a Uri object. */
    protected final Uri pathUri;

    public ApiRequest(LiveConnectSession session,
                      HttpClient client,
                      ResponseHandler<ResponseType> responseHandler,
                      String path) {
        this(session, client, responseHandler, path, ResponseCodes.SUPPRESS, Redirects.SUPPRESS);
    }

    /**
     * Constructs a new instance of an ApiRequest and initializes its member variables
     *
     * @param session that contains the access_token
     * @param client to make Http Requests on
     * @param responseHandler to handle the response
     * @param path of the request. it can be relative or absolute.
     */
    public ApiRequest(LiveConnectSession session,
                      HttpClient client,
                      ResponseHandler<ResponseType> responseHandler,
                      String path,
                      ResponseCodes responseCodes,
                      Redirects redirects) {
        assert session != null;
        assert client != null;
        assert responseHandler != null;
        assert !TextUtils.isEmpty(path);

        this.session = session;
        this.client = client;
        this.observers = new ArrayList<Observer>();
        this.responseHandler = responseHandler;
        this.path = path;
        this.pathUri = Uri.parse(path);

        final int queryStart = path.indexOf("?");
        final String pathWithoutQuery = path.substring(0, queryStart != -1 ? queryStart : path.length());
        final Uri uriWithoutQuery = Uri.parse(pathWithoutQuery);
        final String query;
        if (queryStart != -1) {
            query = path.substring(queryStart + 1, path.length());
        } else {
            query = "";
        }

        UriBuilder builder;

        if (uriWithoutQuery.isAbsolute()) {
            // if the path is absolute we will just use that entire path
            builder = UriBuilder.newInstance(uriWithoutQuery)
                                .query(query);
        } else {
            // if it is a relative path then we should use the config's API URI,
            // which is usually something like https://apis.live.net/v5.0
            builder = UriBuilder.newInstance(Config.INSTANCE.getApiUri())
                                .appendToPath(uriWithoutQuery.getEncodedPath())
                                .query(query);
        }

        responseCodes.setQueryParameterOn(builder);
        redirects.setQueryParameterOn(builder);

        this.requestUri = builder;
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Performs the Http Request and returns the response from the server
     *
     * @return an instance of ResponseType from the server
     * @throws LiveOperationException if there was an error executing the HttpRequest
     */
    public ResponseType execute() throws LiveOperationException {
        // Let subclass decide which type of request to instantiate
        HttpUriRequest request = this.createHttpRequest();

        request.addHeader(LIVE_LIBRARY_HEADER);

        if (this.session.willExpireInSecs(SESSION_REFRESH_BUFFER_SECS)) {
            this.session.refresh();
        }

        // if the session will soon expire, try to send the request without a token.
        // the request *may* not need the token, let's give it a try rather than
        // risk a request with an invalid token.
        if (!this.session.willExpireInSecs(SESSION_TOKEN_SEND_BUFFER_SECS)) {
            request.addHeader(createAuthroizationHeader(this.session));
        }

        try {
            HttpResponse response = this.client.execute(request);

            for (Observer observer : this.observers) {
                observer.onComplete(response);
            }

            return this.responseHandler.handleResponse(response);
        } catch (ClientProtocolException e) {
            throw new LiveOperationException(ErrorMessages.SERVER_ERROR, e);
        } catch (IOException e) {
            // The IOException could contain a JSON object body
            // (see InputStreamResponseHandler.java). If it does,
            // we want to throw an exception with its message. If it does not, we want to wrap
            // the IOException.
            try {
                new JSONObject(e.getMessage());
                throw new LiveOperationException(e.getMessage());
            } catch (JSONException jsonException) {
                throw new LiveOperationException(ErrorMessages.SERVER_ERROR, e);
            }
        }
    }

    /** @return the HTTP method being performed by the request */
    public abstract String getMethod();

    /** @return the path of the request */
    public String getPath() {
        return this.path;
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Factory method that allows subclasses to choose which type of request will
     * be performed.
     *
     * @return the HttpRequest to perform
     * @throws LiveOperationException if there is an error creating the HttpRequest
     */
    protected abstract HttpUriRequest createHttpRequest() throws LiveOperationException;
}
