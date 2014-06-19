//------------------------------------------------------------------------------
// Copyright 2014 Microsoft Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

/**
 * OAuthRespresent a response from an OAuth server.
 * Known implementors are OAuthSuccessfulResponse and OAuthErrorResponse.
 * Different OAuthResponses can be determined by using the OAuthResponseVisitor.
 */
interface OAuthResponse {

    /**
     * Calls visit() on the visitor.
     * This method is used to determine which OAuthResponse is being returned
     * without using instance of.
     *
     * @param visitor to visit the given OAuthResponse
     */
    public void accept(OAuthResponseVisitor visitor);

}
