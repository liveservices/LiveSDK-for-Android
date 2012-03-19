//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

public interface LiveUploadOperationListener {
    public void onUploadCompleted(LiveOperation operation);

    public void onUploadFailed(LiveOperationException exception,
                               LiveOperation operation);

    public void onUploadProgress(int totalBytes,
                                 int bytesRemaining,
                                 LiveOperation operation);
}
