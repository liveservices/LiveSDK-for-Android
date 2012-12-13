package com.microsoft.live.constants;

public final class ErrorMessages {

    public static final String MISSING_REQUIRED_PARAMETER =
            "The request entity body is missing a required parameter. " +
            "The request must include at least one of these parameters: %1$s.";

    public static final String MISSING_REQUIRED_PARAMETER_2 =
            "The request entity body is missing the required parameter: %1$s. " +
            "Required parameters include: %1$s.";

    public static final String PARAMETER_NOT_VALID =
            "The value of input resource ID parameter '%s' isn't valid. " +
            "The expected value for this parameter is a resource ID for one of these types: %s.";

    public static final String RESOURCE_DOES_NOT_EXIST =
            "The resource '%s' doesn't exist.";

    public static final String URL_NOT_VALID =
            "The URL contains the path '%s', which isn't supported.";

    private ErrorMessages() { throw new AssertionError(); }
}
