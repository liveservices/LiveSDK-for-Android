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

    /**
     * Leaves any existing overwrite query parameter on appends this overwrite
     * to the given UriBuilder.
     */
    void appendQueryParameterOnTo(UriBuilder uri) {
        uri.appendQueryParameter(QueryParameters.OVERWRITE, this.overwriteQueryParamValue());
    }

    abstract protected String overwriteQueryParamValue();
}
