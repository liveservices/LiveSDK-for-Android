package com.microsoft.live.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;

public class MockHttpResponse implements HttpResponse {

    private List<Header> headers;
	private HttpEntity httpEntity;
	private StatusLine statusLine;

	{
	    this.headers = new ArrayList<Header>();
	}

	public MockHttpResponse() { }

	public MockHttpResponse(HttpEntity httpEntity, StatusLine statusLine) {
		this.httpEntity = httpEntity;
		this.statusLine = statusLine;
	}

	@Override
	public void addHeader(Header header) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addHeader(String name, String value) {
	    this.headers.add(new BasicHeader(name, value));
	}

	@Override
	public boolean containsHeader(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Header[] getAllHeaders() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpEntity getEntity() {
		if (this.httpEntity != null) {
			return this.httpEntity;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Header getFirstHeader(String name) {
	    for (Header header : this.headers) {
	        if (header.getName().equals(name)) {
	            return header;
	        }
	    }

	    return null;
	}

	@Override
	public Header[] getHeaders(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Header getLastHeader(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getLocale() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpParams getParams() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ProtocolVersion getProtocolVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public StatusLine getStatusLine() {
		return this.statusLine;
	}

	@Override
	public HeaderIterator headerIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HeaderIterator headerIterator(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeHeader(Header header) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeHeaders(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEntity(HttpEntity entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeader(Header header) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeader(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeaders(Header[] headers) {
		throw new UnsupportedOperationException();
	}

	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}

	@Override
	public void setLocale(Locale loc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setParams(HttpParams params) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setReasonPhrase(String reason) throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatusCode(int code) throws IllegalStateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatusLine(ProtocolVersion ver, int code) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatusLine(ProtocolVersion ver, int code, String reason) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatusLine(StatusLine statusline) {
		this.statusLine = statusline;
	}
}
