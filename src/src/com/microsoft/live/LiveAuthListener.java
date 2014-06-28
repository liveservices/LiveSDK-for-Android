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
 * Handles callback methods for LiveAuthClient init, login, and logout methods.
 * Returns the * status of the operation when onAuthComplete is called. If there was an error
 * during the operation, onAuthError is called with the exception that was thrown.
 */
public interface LiveAuthListener {

    /**
     * Invoked when the operation completes successfully.
     *
     * @param status The {@link LiveStatus} for an operation. If successful, the status is
     *               CONNECTED. If unsuccessful, NOT_CONNECTED or UNKNOWN are returned.
     * @param session The {@link LiveConnectSession} from the {@link LiveAuthClient}.
     * @param userState An arbitrary object that is used to determine the caller of the method.
     */
    public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState);

    /**
     * Invoked when the method call fails.
     *
     * @param exception The {@link LiveAuthException} error.
     * @param userState An arbitrary object that is used to determine the caller of the method.
     */
    public void onAuthError(LiveAuthException exception, Object userState);
}
