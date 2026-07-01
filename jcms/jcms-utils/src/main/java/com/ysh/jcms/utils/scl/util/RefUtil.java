package com.ysh.jcms.utils.scl.util;

import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Utility for parsing CMS references like "LD0/LLN0", "LD0/LLN0.Mod.stVal"
 * and resolving them against the SCL model.
 *
 * <p>Also provides helpers for extracting strings from PER-decoded byte arrays
 * and computing {@code referenceAfter} pagination indices.
 */
public final class RefUtil {

    private RefUtil() {}

    // ─────────────────────────────────────────────
    //  Reference parsing
    // ─────────────────────────────────────────────

    /** Parsed result of a reference string like "LD0/LLN0.DO.SDI.DA". */
    public static final class RefParts {
        public final String ldName;
        public final String lnName;
        public final String doName;   // null for LN-level refs
        public final String sdiName;  // null for DO-level refs
        public final String daName;   // null for SDI-level refs

        /** Full LN reference "LD0/LLN0". */
        public String lnRef() { return ldName + "/" + lnName; }

        /** Full DO reference "LD0/LLN0.DO", or null if DO-level. */
        public String doRef() { return doName != null ? ldName + "/" + lnName + "." + doName : null; }

        public RefParts(String ldName, String lnName, String doName, String sdiName, String daName) {
            this.ldName = ldName;
            this.lnName = lnName;
            this.doName = doName;
            this.sdiName = sdiName;
            this.daName = daName;
        }
    }

    /**
     * Parse a reference of the form "LD0/LLN0", "LD0/LLN0.DO", "LD0/LLN0.DO.DA",
     * or "LD0/LLN0.DO.SDI.BDA".
     *
     * @return parsed parts, or null if the format is invalid
     */
    public static RefParts parse(String ref) {
        if (ref == null || ref.isEmpty()) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;
        String sdiName = parts.length > 3 ? parts[2] : null;
        String daName = parts.length > 2 ? (parts.length > 3 ? parts[3] : parts[2]) : null;
        return new RefParts(ldName, lnName, doName, sdiName, daName);
    }

    // ─────────────────────────────────────────────
    //  SCL model resolution
    // ─────────────────────────────────────────────

    /** Result of resolving a reference against the SCL model. */
    public static final class ResolveResult {
        public final RefParts ref;
        public final SclLDevice device;
        public final SclLN ln;
        public final SclDOI doi;   // null for LN-level refs
        public final SclSDI sdi;   // null for DO-level refs

        public ResolveResult(RefParts ref, SclLDevice device, SclLN ln, SclDOI doi, SclSDI sdi) {
            this.ref = ref;
            this.device = device;
            this.ln = ln;
            this.doi = doi;
            this.sdi = sdi;
        }
    }

    /**
     * Resolve a reference to LN (and optionally DOI) from the SCL model.
     *
     * @return result with device/ln/doi populated, or null if any part not found
     */
    public static ResolveResult resolve(SclServer server, String ref) {
        RefParts parts = parse(ref);
        if (parts == null) return null;
        return resolve(server, parts);
    }

    /**
     * Resolve parsed parts to LN (and optionally DOI) from the SCL model.
     */
    public static ResolveResult resolve(SclServer server, RefParts parts) {
        SclLDevice device = server.findLDeviceByInst(parts.ldName);
        if (device == null) return null;
        SclLN ln = device.findLnByFullName(parts.lnName);
        if (ln == null) return null;
        SclDOI doi = parts.doName != null ? ln.findDoiByName(parts.doName) : null;
        if (parts.doName != null && doi == null) return null;
        SclSDI sdi = parts.sdiName != null ? (doi != null ? doi.findSdiByName(parts.sdiName) : null) : null;
        if (parts.sdiName != null && sdi == null) return null;
        return new ResolveResult(parts, device, ln, doi, sdi);
    }

    // ─────────────────────────────────────────────
    //  referenceAfter pagination
    // ─────────────────────────────────────────────

    /**
     * Find the starting index for {@code referenceAfter} pagination.
     *
     * @param entries the complete list
     * @param after   the referenceAfter value, or null to start from beginning
     * @return the starting index (0 if after is null or empty)
     */
    public static int afterIndex(List<String> entries, String after) {
        if (after == null || after.isEmpty()) return 0;
        int idx = entries.indexOf(after);
        return idx >= 0 ? idx + 1 : -1;
    }

    /**
     * Variant that compares entries using a key extractor.
     *
     * @param <T>      entry type
     * @param entries  the complete list
     * @param after    the referenceAfter value, or null
     * @param keyFn    function to extract the comparison key from an entry
     * @return starting index, or -1 if after not found
     */
    public static <T> int afterIndex(List<T> entries, String after, java.util.function.Function<T, String> keyFn) {
        if (after == null || after.isEmpty()) return 0;
        for (int i = 0; i < entries.size(); i++) {
            if (keyFn.apply(entries.get(i)).equals(after)) return i + 1;
        }
        return -1;
    }

    // ─────────────────────────────────────────────
    //  String extraction from PER-decoded arrays
    // ─────────────────────────────────────────────

    /**
     * Extract a non-empty String from a PER-decoded byte array.
     *
     * @return the string, or null if the array is empty
     */
    public static String str(byte[] arr) {
        if (arr == null || arr.length == 0) return null;
        return new String(arr, StandardCharsets.UTF_8);
    }

    /**
     * Extract a String from a PER-decoded CmsUint8Array.
     *
     * @return the string, or null if len == 0
     */
    public static String str(com.ysh.jcms.data.string.CmsUint8Array arr) {
        if (arr == null || arr.len == 0) return null;
        return new String(arr.value(), StandardCharsets.UTF_8);
    }

    /**
     * Extract an optional String field controlled by a Present marker.
     *
     * @return the string, or null if not present or empty
     */
    public static String opt(com.ysh.jcms.data.scalar.CmsBoolean present, com.ysh.jcms.data.string.CmsUint8Array arr) {
        if (!present.value() || arr == null || arr.len == 0) return null;
        return new String(arr.value(), StandardCharsets.UTF_8);
    }
}
