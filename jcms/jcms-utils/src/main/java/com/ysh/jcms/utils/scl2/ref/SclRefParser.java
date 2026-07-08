package com.ysh.jcms.utils.scl2.ref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SclRefParser {

    private static final Pattern REF_PATTERN = Pattern.compile(
            "([^/]+)/([^.]+)(?:\\.([^.]+)(?:\\.([^.]+))?)?(?:\\[([^]]+)\\])?");

    private SclRefParser() {
    }

    public static SclRef parse(String ref) {
        // TODO: implement proper parsing
        Matcher matcher = REF_PATTERN.matcher(ref);
        if (matcher.matches()) {
            String ldName = matcher.group(1);
            String lnName = matcher.group(2);
            String doName = matcher.group(3);
            String daName = matcher.group(4);
            String fc = matcher.group(5);
            return new SclRef(ldName, lnName, doName, daName, fc, ref);
        }
        return new SclRef(null, null, null, null, null, ref);
    }
}
