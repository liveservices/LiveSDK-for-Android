//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

public interface LiveOperationListener {
    public void onComplete(LiveOperation operation);
    public void onError(LiveOperationException exception,
                        LiveOperation operation);
}
