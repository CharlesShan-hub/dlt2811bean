package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsSubReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    public CmsSubReference() {
        super(129, true);
    }

    @Override
    public CmsSubReference value(String data) {
        return (CmsSubReference) super.value(data);
    }

    /**
     * Build from dot-separated segments.
     * <pre>
     *   CmsSubReference.of("LN", "DO", "DA", "BDA")  → "LN.DO.DA.BDA"
     *   CmsSubReference.of("DA", "BDA")              → "DA.BDA"
     * </pre>
     */
    public static CmsSubReference of(String... segments) {
        return (CmsSubReference) new CmsSubReference().value(String.join(".", segments));
    }

    /** Number of dot-separated segments. */
    public int segmentCount() {
        String s = asString();
        if (s.isEmpty()) return 0;
        return (int) s.chars().filter(c -> c == '.').count() + 1;
    }

    /** Get the i-th segment (0-based). */
    public String segment(int i) {
        String s = asString();
        String[] parts = s.split("\\.", -1);
        if (i < 0 || i >= parts.length) return "";
        return parts[i];
    }

    /** All segments as an array. */
    public String[] segments() {
        String s = asString();
        if (s.isEmpty()) return new String[0];
        return s.split("\\.", -1);
    }

    private String asString() {
        return new String(value(), java.nio.charset.StandardCharsets.UTF_8).trim();
    }

    public static class ByValue extends CmsSubReference implements Structure.ByValue {}
}