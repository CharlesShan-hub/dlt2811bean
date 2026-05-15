package com.ysh.dlt2811bean.scl2.ref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SclRefParser {

    private static final Pattern REF_PATTERN = Pattern.compile(
        "^([A-Za-z][A-Za-z0-9_]*)/([A-Za-z][A-Za-z0-9_]*)(?:\\.([A-Za-z][A-Za-z0-9_]*)(?:\\.([A-Za-z][A-Za-z0-9_]*))?)?$"
    );

    private static final Pattern REF_WITH_FC_PATTERN = Pattern.compile(
        "^([A-Za-z][A-Za-z0-9_]*)/([A-Za-z][A-Za-z0-9_]*)\\.([A-Za-z][A-Za-z0-9_]*)\\.([A-Za-z][A-Za-z0-9_]*)\\[([A-Za-z]+)\\]$"
    );

    private SclRefParser() {}

    public static SclRef parse(String ref) {
        if (ref == null || ref.isBlank()) {
            throw new IllegalArgumentException("Reference cannot be null or blank");
        }

        String trimmed = ref.trim();

        Matcher fcMatcher = REF_WITH_FC_PATTERN.matcher(trimmed);
        if (fcMatcher.matches()) {
            return new SclRef(
                fcMatcher.group(1),
                fcMatcher.group(2),
                fcMatcher.group(3),
                fcMatcher.group(4),
                fcMatcher.group(5),
                trimmed
            );
        }

        Matcher matcher = REF_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            return new SclRef(
                matcher.group(1),
                matcher.group(2),
                matcher.group(3),
                matcher.group(4),
                null,
                trimmed
            );
        }

        throw new IllegalArgumentException("Invalid SCL reference format: " + ref);
    }

    public static boolean isValid(String ref) {
        if (ref == null || ref.isBlank()) return false;
        String trimmed = ref.trim();
        return REF_PATTERN.matcher(trimmed).matches() || REF_WITH_FC_PATTERN.matcher(trimmed).matches();
    }

    public static String extractLnReference(String ref) {
        SclRef parsed = parse(ref);
        return parsed.getLnReference();
    }

    public static String extractDoReference(String ref) {
        SclRef parsed = parse(ref);
        return parsed.getDoReference();
    }
}
