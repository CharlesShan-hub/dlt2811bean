package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsObjectReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    public CmsObjectReference() {
        super(129, true);
    }

    @Override
    public CmsObjectReference value(String data) {
        return (CmsObjectReference) super.value(data);
    }

    /** LDName — the logical device name.
     * <p>
     * If the LDName is explicit (model defines {@code ldName}), it contains no '/'.
     * Otherwise LDName is composed as {@code IedName/ldInst}.
     * <p>
     * Examples:
     * <pre>
     *   "STATION_CTRL/CILO.Clc1"        → ldName = "STATION_CTRL"
     *   "P1A1/PROT/GGIO1.SPCSO1"        → ldName = "P1A1/PROT"  (composed)
     * </pre>
     */
    public String ldName() {
        String s = asString();
        int lastSlash = s.lastIndexOf('/');
        if (lastSlash < 0) return "";
        return s.substring(0, lastSlash);
    }

    /** IedName — the first part of a composed LDName.
     * <p>
     * Only present when LDName is composed as {@code IedName/ldInst}, empty otherwise.
     * <pre>
     *   "P1A1/PROT/GGIO1.SPCSO1"  → iedName = "P1A1"
     *   "STATION_CTRL/CILO.Clc1"  → iedName = ""  (explicit ldName, no IedName)
     * </pre>
     */
    public String iedName() {
        String s = asString();
        int firstSlash = s.indexOf('/');
        int lastSlash = s.lastIndexOf('/');
        if (lastSlash < 0) return "";
        // If there are exactly two slashes, the first segment is IedName
        // If there's only one slash, LDName is explicit → no IedName
        if (firstSlash == lastSlash) return "";
        return s.substring(0, firstSlash);
    }

    /** ldInst — the second part of a composed LDName.
     * <p>
     * Only present when LDName is composed as {@code IedName/ldInst}, empty otherwise.
     * <pre>
     *   "P1A1/PROT/GGIO1.SPCSO1"  → ldInst = "PROT"
     *   "STATION_CTRL/CILO.Clc1"  → ldInst = ""  (explicit ldName)
     * </pre>
     */
    public String ldInst() {
        String s = asString();
        int firstSlash = s.indexOf('/');
        int lastSlash = s.lastIndexOf('/');
        if (lastSlash < 0) return "";
        if (firstSlash == lastSlash) return "";
        return s.substring(firstSlash + 1, lastSlash);
    }

    /** LNName — the logical node name after LDName.
     * <p>
     * Everything after the last '/' and before the first '.' (or end of string).
     * <pre>
     *   "P1A1/PROT/GGIO1.SPCSO1"  → lnName = "GGIO1"
     *   "STATION_CTRL/CILO"       → lnName = "CILO"
     * </pre>
     */
    public String lnName() {
        String s = asString();
        int lastSlash = s.lastIndexOf('/');
        if (lastSlash < 0) return "";
        String rest = s.substring(lastSlash + 1);
        int dot = rest.indexOf('.');
        if (dot >= 0) rest = rest.substring(0, dot);
        return rest;
    }

    /** DataSet name — for @DataSetName references. */
    public String dataSetName() {
        String s = asString();
        if (!s.startsWith("@") || s.length() < 2) return "";
        return s.substring(1);
    }

    /** Create a reference: LDName/LNName[.Name[....]]. */
    public static CmsObjectReference of(String ldName, String lnName, String... names) {
        StringBuilder sb = new StringBuilder();
        sb.append(ldName).append('/').append(lnName);
        for (String n : names) sb.append('.').append(n);
        return (CmsObjectReference) new CmsObjectReference().value(sb.toString());
    }

    /** Create a dataset reference: @DataSetName. */
    public static CmsObjectReference dataSet(String name) {
        return (CmsObjectReference) new CmsObjectReference().value("@" + name);
    }

    private String asString() {
        return new String(value(), java.nio.charset.StandardCharsets.UTF_8).trim();
    }

    public static class ByValue extends CmsObjectReference implements Structure.ByValue {}
}