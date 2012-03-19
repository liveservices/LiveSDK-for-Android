//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

public interface LiveAuthListener {
    public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState);
    public void onAuthError(LiveAuthException exception, Object userState);
}
