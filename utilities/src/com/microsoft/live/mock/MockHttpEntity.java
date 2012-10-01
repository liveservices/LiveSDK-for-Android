package com.microsoft.live.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

public class MockHttpEntity implements HttpEntity {

	private IllegalStateException illegalStateException;
	private InputStream inputStream;
	private IOException ioException;

	public MockHttpEntity() { }

	public MockHttpEntity(IllegalStateException illegalStateException) {
		this.illegalStateException = illegalStateException;
	}

	public MockHttpEntity(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public MockHttpEntity(IOException ioException) {
		this.ioException = ioException;
	}

	@Override
	public void consumeContent() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		if (this.inputStream != null) {
			return this.inputStream;
		} else if (this.ioException != null) {
			throw this.ioException;
		} else if (this.illegalStateException != null) {
			throw this.illegalStateException;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Header getContentEncoding() {
		return new BasicHeader("Content-encoding", HTTP.UTF_8);
	}

	@Override
	public long getContentLength() {
	    return -1L;
	}

	@Override
	public Header getContentType() {
		return null;
	}

	@Override
	public boolean isChunked() {
		return true;
	}

	@Override
	public boolean isRepeatable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isStreaming() {
		throw new UnsupportedOperationException();
	}

	public void setIllegalStateException(IllegalStateException illegalStateException) {
		this.illegalStateException = illegalStateException;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setIoException(IOException ioException) {
		this.ioException = ioException;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		throw new UnsupportedOperationException();
	}

}
