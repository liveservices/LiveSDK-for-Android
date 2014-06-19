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
 * Called when an operation finishes or has an error.
 */
public interface LiveOperationListener {

    /**
     * Called when the associated Representational State Transfer (REST) API operation call
     * completes.
     * @param operation The {@link LiveOperation} object.
     */
    public void onComplete(LiveOperation operation);

    /**
     * Called when the associated Representational State Transfer (REST) operation call fails.
     * @param exception The error returned by the REST operation call.
     * @param operation The {@link LiveOperation} object.
     */
    public void onError(LiveOperationException exception, LiveOperation operation);
}
