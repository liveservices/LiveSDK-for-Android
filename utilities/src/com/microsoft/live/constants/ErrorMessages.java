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

package com.microsoft.live.constants;

public final class ErrorMessages {

    public static final String MISSING_REQUIRED_PARAMETER =
            "The request entity body is missing a required parameter. " +
            "The request must include at least one of these parameters: %1$s.";

    public static final String MISSING_REQUIRED_PARAMETER_2 =
            "The request entity body is missing the required parameter: %1$s. " +
            "Required parameters include: %1$s.";

    public static final String PARAMETER_NOT_VALID =
            "The value of input resource ID parameter '%s' isn't valid. " +
            "The expected value for this parameter is a resource ID for one of these types: %s.";

    public static final String RESOURCE_DOES_NOT_EXIST =
            "The resource '%s' doesn't exist.";

    public static final String URL_NOT_VALID =
            "The URL contains the path '%s', which isn't supported.";

    private ErrorMessages() { throw new AssertionError(); }
}
