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
