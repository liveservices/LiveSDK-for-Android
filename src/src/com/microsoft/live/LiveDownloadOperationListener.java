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
 * Represents any functionality related to downloads that works with the Live Connect
 * Representational State Transfer (REST) API.
 */
public interface LiveDownloadOperationListener {

    /**
     * Called when the associated download operation call completes.
     * @param operation The {@link LiveDownloadOperation} object.
     */
    public void onDownloadCompleted(LiveDownloadOperation operation);

    /**
     * Called when the associated download operation call fails.
     * @param exception The error returned by the REST operation call.
     * @param operation The {@link LiveDownloadOperation} object.
     */
    public void onDownloadFailed(LiveOperationException exception,
                                 LiveDownloadOperation operation);

    /**
     * Updates the progression of the download.
     * @param totalBytes The total bytes downloaded.
     * @param bytesRemaining The bytes remaining to download.
     * @param operation The {@link LiveDownloadOperation} object.
     */
    public void onDownloadProgress(int totalBytes,
                                   int bytesRemaining,
                                   LiveDownloadOperation operation);
}
