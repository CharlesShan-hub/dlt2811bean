package com.ysh.jcms.utils.scl.conformance;

/**
 * SubNetwork names recommended by Q/GDW 1396-2012 §6.5.1.
 * <p>
 * The whole-station network is preferably split into a station bus and a
 * process bus, named "Subnetwork_Stationbus" and "Subnetwork_Processbus".
 */
public enum GwSubNetwork {

    Subnetwork_Stationbus("Station bus"), Subnetwork_Processbus("Process bus");

    private final String desc;

    GwSubNetwork(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    /**
     * Whether the given SubNetwork name matches one of the reserved names.
     *
     * @param name
     *            SubNetwork name
     * @return true if the name is one of the reserved ones
     */
    public static boolean isAllowed(String name) {
        for (GwSubNetwork sn : values()) {
            if (sn.name().equals(name))
                return true;
        }
        return false;
    }

    /** All reserved names joined for error messages. */
    public static String allowedNames() {
        StringBuilder sb = new StringBuilder();
        for (GwSubNetwork sn : values()) {
            if (sb.length() > 0)
                sb.append("/");
            sb.append(sn.name());
        }
        return sb.toString();
    }
}
