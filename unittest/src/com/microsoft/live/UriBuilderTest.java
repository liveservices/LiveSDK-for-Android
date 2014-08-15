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

import android.test.InstrumentationTestCase;

public class UriBuilderTest extends InstrumentationTestCase {
    
    public void testAppendEmptyPath() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.path("bar");
        builder.appendToPath("");
        
        assertEquals("http://foo.com/bar", builder.toString());
    }
    
    public void testEmptyPathEmptyAppendPath() {
        UriBuilder builder = new UriBuilder();
        
        builder.scheme("http");
        builder.host("foo.com");
        builder.path("");
        builder.appendToPath("");
        
        assertEquals("http://foo.com", builder.toString());
    }
    
    public void testAppendSingleForwardSlash() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.path("bar");
        builder.appendToPath("/");
        
        assertEquals("http://foo.com/bar/", builder.toString());
    }
    
    public void testAppendOnToPathThatEndsInSlash() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.path("bar/");
        builder.appendToPath("test");
        
        assertEquals("http://foo.com/bar/test", builder.toString());
    }
    
    public void testAppendPathThatBeginsWithSlashOnToPathThatEndsInSlash() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.path("bar/");
        builder.appendToPath("/test");
        
        assertEquals("http://foo.com/bar/test", builder.toString());
    }
    
    public void testSetQueryWithNullQueryString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query(null);
        
        assertEquals("http://foo.com", builder.toString());
    }
    
    public void testSetQueryWithDuplicates() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k1=v1");
        
        assertEquals("http://foo.com?k1=v1&k2=v2&k1=v1", builder.toString());
    }
    
    /**
     * Storage returns URLs with a query parameter that has no value.
     */
    public void testSetQueryWithKeyAndNoValue() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.query("download");
        
        assertEquals("http://foo.com?download", builder.toString());
    }
    
    public void testSetQueryWithEmptyString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.query("");
        
        assertEquals("http://foo.com", builder.toString());
    }

    public void testSetQueryStringOnePair() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.query("k1=v1");
        
        assertEquals("http://foo.com?k1=v1", builder.toString());
    }
    
    public void testSetQueryStringMultiplePairs() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.query("k1=v1&k2=v2");
        
        assertEquals("http://foo.com?k1=v1&k2=v2", builder.toString());
    }
    
    public void testSetQueryStringRemoveExisting() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k2=v2");
        builder.query("k1=v1");
        
        int indexOfQuestionMark = builder.toString().indexOf("?");
        String queryString = builder.toString().substring(indexOfQuestionMark + 1);
        
        assertEquals("k1=v1", queryString);
    }
    
    public void testRemoveQueryParametersExistingKey() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k3=v3");
        
        builder.removeQueryParametersWithKey("k2");
        
        assertEquals("http://foo.com?k1=v1&k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersWithNoValue() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2&k3=v3");
        
        builder.removeQueryParametersWithKey("k2");
        
        assertEquals("http://foo.com?k1=v1&k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersDoesNotExist() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k3=v3");
        
        builder.removeQueryParametersWithKey("k4");
        
        assertEquals("http://foo.com?k1=v1&k2=v2&k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersNullKey() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k3=v3");
        
        builder.removeQueryParametersWithKey(null);
        
        assertEquals("http://foo.com?k1=v1&k2=v2&k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersEmptyStringKey() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k3=v3");
        
        builder.removeQueryParametersWithKey("");
        
        assertEquals("http://foo.com?k1=v1&k2=v2&k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersMultipleKeys() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k1=v2&k3=v3");
        
        builder.removeQueryParametersWithKey("k1");
        
        assertEquals("http://foo.com?k3=v3", builder.toString());
    }
    
    public void testRemoveQueryParametersAll() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1&k2=v2&k3=v3");
        
        builder.removeQueryParametersWithKey("k1");
        builder.removeQueryParametersWithKey("k2");
        builder.removeQueryParametersWithKey("k3");
        
        assertEquals("http://foo.com", builder.toString());
    }
    
    public void testRemoveQueryParametersFromNoQueryParameters() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.removeQueryParametersWithKey("k1");
        
        assertEquals("http://foo.com", builder.toString());
    }
    
    public void testAppendQueryParameterOnNoExistingQueryString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.appendQueryParameter("k1", "v1");
        
        assertEquals("http://foo.com?k1=v1", builder.toString());
    }
    
    public void testAppendQueryParameterOnExistingQueryString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1");
        
        builder.appendQueryParameter("k2", "v2");
        
        assertEquals("http://foo.com?k1=v1&k2=v2", builder.toString());
    }
    
    public void testAppendQueryParameterCreateDuplicates() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1");
        
        builder.appendQueryParameter("k1", "v1");
        
        assertEquals("http://foo.com?k1=v1&k1=v1", builder.toString());
    }
    
    public void testAppendQueryStringMultipleParameters() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.appendQueryString("k1=v1&k2=v2");
        
        assertEquals("http://foo.com?k1=v1&k2=v2", builder.toString());
    }
    
    public void testAppendQueryStringOnNoExistingQueryString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        
        builder.appendQueryString("k1=v1");
        
        assertEquals("http://foo.com?k1=v1", builder.toString());
    }
    
    public void testAppendQueryStringOnExistingQueryString() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1");
        
        builder.appendQueryString("k2=v2");
        
        assertEquals("http://foo.com?k1=v1&k2=v2", builder.toString());
    }
    
    public void testAppendQueryStringCreateDuplicates() {
        UriBuilder builder = new UriBuilder();
        builder.scheme("http");
        builder.host("foo.com");
        builder.query("k1=v1");
        
        builder.appendQueryString("k1=v1");
        
        assertEquals("http://foo.com?k1=v1&k1=v1", builder.toString());
    }
}
