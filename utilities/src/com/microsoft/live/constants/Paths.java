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
//------------------------------------------------------------------------------

package com.microsoft.live.constants;

public final class Paths {
	public static final String ABSOLUTE = "https://apis.live.net/v5.0/me";
	public static final String INVALID = "&Some!Invalid*Path8";
	public static final String ME = "me";
	public static final String ME_CALENDARS = ME + "/calendars";
	public static final String ME_CONTACTS = ME + "/contacts";
	public static final String ME_PICTURE = ME + "/picture";
	public static final String ME_SKYDRIVE = ME + "/skydrive";

	private Paths() { throw new AssertionError(); }
}
