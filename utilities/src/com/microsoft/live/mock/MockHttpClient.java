package com.microsoft.live.mock;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class MockHttpClient implements HttpClient {

	private ClientProtocolException clientProtocolException;
	private Queue<HttpResponse> responseQueue;
	private IOException ioException;

	public MockHttpClient() { }

	public MockHttpClient(ClientProtocolException clientProtocolException) {
		this.clientProtocolException = clientProtocolException;
	}

	public MockHttpClient(HttpResponse httpResponse) {
	    this.responseQueue = new LinkedList<HttpResponse>();
	    this.responseQueue.add(httpResponse);
	}

	public MockHttpClient(IOException ioException) {
		this.ioException = ioException;
	}

	public void addHttpResponse(HttpResponse httpResponse) {
        this.responseQueue.add(httpResponse);
    }

	public void clearHttpResponseQueue() {
	    this.responseQueue.clear();
	}

    @Override
	public HttpResponse execute(HttpHost target, HttpRequest request)
			throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request,
			HttpContext context) throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2) throws IOException,
			ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpResponse execute(HttpUriRequest request) throws IOException,
			ClientProtocolException {
		if (this.ioException != null) {
			throw this.ioException;
		} else if (this.clientProtocolException != null) {
			throw this.clientProtocolException;
		} else if (this.responseQueue != null && !this.responseQueue.isEmpty()) {
		    return this.responseQueue.remove();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public HttpResponse execute(HttpUriRequest request, HttpContext context)
			throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
			throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T execute(HttpUriRequest arg0,
			ResponseHandler<? extends T> arg1, HttpContext arg2)
			throws IOException, ClientProtocolException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClientConnectionManager getConnectionManager() {
		throw new UnsupportedOperationException();
	}

	public HttpResponse getHttpResponse() {
	    return this.responseQueue.peek();
	}

	@Override
	public HttpParams getParams() {
		throw new UnsupportedOperationException();
	}

	public void setClientProtocolException(
			ClientProtocolException clientProtocolException) {
		this.clientProtocolException = clientProtocolException;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
	    this.responseQueue.clear();
		this.responseQueue.add(httpResponse);
	}

	public void setIoException(IOException ioException) {
		this.ioException = ioException;
	}
}
