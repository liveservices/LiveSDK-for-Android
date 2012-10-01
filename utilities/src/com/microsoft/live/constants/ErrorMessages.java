package com.microsoft.live.constants;

public final class ErrorMessages {

    public static final String MISSING_REQUIRED_PARAMETER =
            "The provided request entity body is missing a required parameter. " +
            "The request must include at least one of the following: %1$s.";

    public static final String MISSING_REQUIRED_PARAMETER_2 =
            "The provided request entity body is missing a required parameter %1$s. " +
            "Required parameters are the following: %1$s.";

    public static final String PARAMETER_NOT_VALID =
            "The provided value for the input resource ID parameter '%s' is not valid. " +
            "Expected value is a resource ID for one of the following types: %s.";

    public static final String RESOURCE_DOES_NOT_EXIST =
            "The requested resource '%s' does not exist.";

    public static final String URL_NOT_VALID =
            "The provided URL is not valid. The requested path '%s' is not supported.";


    private ErrorMessages() { throw new AssertionError(); }
}
