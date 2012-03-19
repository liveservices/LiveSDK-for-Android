//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.net.Uri.Builder;
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
            protected void appendQueryParameter(Builder builder) {
                appendSuppressRedirects(builder, Boolean.TRUE);
            }
        }, UNSUPPRESSED {
            @Override
            protected void appendQueryParameter(Builder builder) {
                appendSuppressRedirects(builder, Boolean.FALSE);
            }
        };

        protected abstract void appendQueryParameter(Uri.Builder builder);

        private static void appendSuppressRedirects(Uri.Builder builder, Boolean value) {
            builder.appendQueryParameter(QueryParameters.SUPPRESS_REDIRECTS, value.toString());
        }
    }

    public enum ResponseCodes {
        SUPPRESS {
            @Override
            protected void appendQueryParameter(Builder builder) {
                appendSuppressResponseCodes(builder, Boolean.TRUE);
            }
        }, UNSUPPRESSED {
            @Override
            protected void appendQueryParameter(Builder builder) {
                appendSuppressResponseCodes(builder, Boolean.FALSE);
            }
        };

        protected abstract void appendQueryParameter(Uri.Builder builder);

        private static void appendSuppressResponseCodes(Uri.Builder builder, Boolean value) {
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

        String tokenType = OAuth.TokenType.BEARER.toString().toLowerCase();
        String value = TextUtils.join(" ", new String[]{tokenType, accessToken});
        return new BasicHeader(AUTH.WWW_AUTH_RESP, value);
    }

    private final HttpClient client;
    private final List<Observer> observers;
    private final String path;
    private final ResponseHandler<ResponseType> responseHandler;
    private final LiveConnectSession session;
    protected final Uri.Builder requestUri;

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

        Uri.Builder builder;
        Uri pathUri = Uri.parse(path);
        if (pathUri.isAbsolute()) {
            builder = pathUri.buildUpon();
        } else {
            builder = Config.INSTANCE.getApiUri()
                                     .buildUpon()
                                     .appendEncodedPath(pathUri.getPath())
                                     .encodedQuery(pathUri.getQuery());
        }

        // we need to see if we have any extra query params
        // that need to be added on to the request uri
        String apiQuery = Config.INSTANCE.getApiUri().getEncodedQuery();
        String pathQuery = builder.build().getEncodedQuery();
        String query;
        if (apiQuery != null && pathQuery != null) {
            query = TextUtils.join("&", new String[]{apiQuery, pathQuery});
        } else if (apiQuery != null) {
            query = apiQuery;
        } else if (pathQuery != null) {
            query = pathQuery;
        } else {
            query = "";
        }

        builder.encodedQuery(query);

        responseCodes.appendQueryParameter(builder);
        redirects.appendQueryParameter(builder);

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
