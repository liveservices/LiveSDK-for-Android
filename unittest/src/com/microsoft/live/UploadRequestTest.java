package com.microsoft.live;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.microsoft.live.constants.JsonKeys;
import com.microsoft.live.constants.Paths;
import com.microsoft.live.mock.MockHttpEntity;
import com.microsoft.live.mock.MockHttpResponse;

import android.test.InstrumentationTestCase;

public class UploadRequestTest extends InstrumentationTestCase {
    
    /**
     * WinLive 633441: Make sure the query parameters on path get sent to
     * the HTTP PUT part of the upload.
     */
    public void testSendPathQueryParameterToHttpPut() throws Throwable {
        JSONObject jsonResponseBody = new JSONObject();
        jsonResponseBody.put(JsonKeys.UPLOAD_LOCATION, "http://test.com/location");
        InputStream responseStream =
                new ByteArrayInputStream(jsonResponseBody.toString().getBytes());
        MockHttpEntity responseEntity = new MockHttpEntity(responseStream);
        BasicStatusLine ok = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "");
        final MockHttpResponse uploadLocationResponse = new MockHttpResponse(responseEntity, ok);
        
        HttpClient client = new HttpClient() {
            /** the first request to the client is the upload location request. */
            boolean uploadLocationRequest = true;
            
            @Override
            public HttpResponse execute(HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                
                if (uploadLocationRequest) {
                    uploadLocationRequest = false;
                    return uploadLocationResponse;
                }
                
                // This is really the only part we care about in this test.
                // That the 2nd request's uri has foo=bar in the query string.
                URI uri = request.getURI();
                assertEquals("foo=bar&overwrite=choosenewname", uri.getQuery());
                
                // for the test it doesn't matter what it contains, as long as it has valid json.
                // just return the previous reponse.
                return uploadLocationResponse;
            }
            
            @Override
            public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            } 
            
            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1, HttpContext arg2) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2, HttpContext arg3) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            } 
            
            @Override
            public ClientConnectionManager getConnectionManager() { throw new UnsupportedOperationException(); }

            @Override
            public HttpParams getParams() { throw new UnsupportedOperationException(); }
        };
        
        LiveConnectSession session = TestUtils.newMockLiveConnectSession();
        
        HttpEntity entity = new MockHttpEntity();
        String path = Paths.ME_SKYDRIVE + "?foo=bar";
        String filename = "filename";
        
        UploadRequest uploadRequest =
                new UploadRequest(session, client, path, entity, filename, OverwriteOption.Rename);
        
        uploadRequest.execute();
    }
}
