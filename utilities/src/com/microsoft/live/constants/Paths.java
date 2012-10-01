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
