package com.ysh.jcms.utils.scl.conformance;

import java.util.regex.Pattern;

/**
 * Logical device instance names defined by Q/GDW 1396-2012 §7.1.3.
 * <p>
 * Logical devices are divided by function; the instance name (inst) must be one
 * of the ten reserved names. When more than one LD of the same kind exists, a
 * two-digit numeric suffix is appended, e.g. PIGO01, PIGO02.
 */
public enum GwLdInst {

    LD0("Common LD"),
    MEAS("Measurement LD"),
    PROT("Protection LD"),
    CTRL("Control LD"),
    PIGO("GOOSE process-bus LD"),
    PISV("SV process-bus LD"),
    RPIT("Smart terminal LD (Remote Process Interface Terminal)"),
    RCD("Fault recorder LD"),
    MUGO("Merging unit GOOSE LD"),
    MUSV("Merging unit SV LD");

    /** Two-digit numeric suffix, e.g. "01" in PIGO01. */
    private static final Pattern SUFFIX_2DIGIT = Pattern.compile("\\d{2}");

    private final String desc;

    GwLdInst(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    /**
     * Whether an LD instance name conforms to §7.1.3: either an exact reserved
     * name, or a reserved name plus a two-digit numeric suffix.
     *
     * @param inst
     *            LD instance name
     * @return true if the name is allowed
     */
    public static boolean isAllowed(String inst) {
        if (inst == null)
            return false;
        if (byName(inst) != null)
            return true;
        if (inst.length() > 2 && SUFFIX_2DIGIT.matcher(inst.substring(inst.length() - 2)).matches()) {
            return byName(inst.substring(0, inst.length() - 2)) != null;
        }
        return false;
    }

    private static GwLdInst byName(String name) {
        for (GwLdInst ld : values()) {
            if (ld.name().equals(name))
                return ld;
        }
        return null;
    }

    /** All reserved names joined for error messages. */
    public static String allowedNames() {
        StringBuilder sb = new StringBuilder();
        for (GwLdInst ld : values()) {
            if (sb.length() > 0)
                sb.append('/');
            sb.append(ld.name());
        }
        return sb.toString();
    }
}
