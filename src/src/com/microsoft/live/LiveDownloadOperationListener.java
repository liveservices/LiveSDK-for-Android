//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

public interface LiveDownloadOperationListener {

    public void onDownloadCompleted(LiveDownloadOperation operation);

    public void onDownloadFailed(LiveOperationException exception,
                                 LiveDownloadOperation operation);

    public void onDownloadProgress(int totalBytes,
                                   int bytesRemaining,
                                   LiveDownloadOperation operation);
}
