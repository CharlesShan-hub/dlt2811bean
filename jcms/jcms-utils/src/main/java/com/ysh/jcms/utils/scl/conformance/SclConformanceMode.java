package com.ysh.jcms.utils.scl.conformance;

/**
 * Conformance checking mode for SCL documents.
 * <p>
 * LOOSE keeps the historical international-standard behaviour (no national-grid
 * checks); STRICT additionally validates the document against the China State
 * Grid enterprise standard Q/GDW 1396-2012 (IEC 61850 engineering application
 * model for protection relays).
 */
public enum SclConformanceMode {

    /** IEC 61850-6 only; no Q/GDW 1396 checks are performed. */
    LOOSE,

    /** Q/GDW 1396 enabled; naming/structure/communication rules are evaluated. */
    STRICT;

    /**
     * Lenient parser for config strings: any value other than "STRICT" falls back
     * to LOOSE, so a typo in the configuration can never break startup.
     *
     * @param s
     *            raw config value (may be null)
     * @return the matching mode, LOOSE by default
     */
    public static SclConformanceMode from(String s) {
        return s != null && "STRICT".equalsIgnoreCase(s.trim()) ? STRICT : LOOSE;
    }
}
