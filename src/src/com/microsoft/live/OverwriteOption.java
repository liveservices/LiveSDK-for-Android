//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//
// Description: See the class level JavaDoc comments.
//------------------------------------------------------------------------------

package com.microsoft.live;

import android.net.Uri.Builder;

/**
 * Enum that specifies what to do during a naming conflict during an upload.
 */
public enum OverwriteOption {

    /** Overwrite the existing file. */
    Overwrite {
        @Override
        protected String overwriteQueryParamValue() {
            return "true";
        }
    },

    /** Do Not Overwrite the existing file and cancel the upload. */
    DoNotOverwrite {
        @Override
        protected String overwriteQueryParamValue() {
            return "false";
        }
    },

    /** Rename the current file to avoid a name conflict. */
    Rename {
        @Override
        protected String overwriteQueryParamValue() {
            return "choosenewname";
        }
    };

    void appendQueryParameter(Builder uri) {
        uri.appendQueryParameter(QueryParameters.OVERWRITE, this.overwriteQueryParamValue());
    }

    abstract protected String overwriteQueryParamValue();
}
