package com.ysh.dlt2811bean.scl2.util;

import com.ysh.dlt2811bean.scl2.model.SclFCDA;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclServer;

import java.util.Objects;

public class SclDataSetResolver {

    private SclDataSetResolver() {}

    /**
     * Builds a full reference string from an FCDA.
     * <p>Format: {@code LDinst/LNFullName.DO[.DA]}
     * <p>For example: {@code C1/MMXU1.Volts.mag} or {@code C1/CSWI1.Pos}
     */
    public static String buildFcdaRef(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        sb.append(fcda.getLdInst()).append("/");
        if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
            sb.append(fcda.getPrefix());
        }
        sb.append(fcda.getLnClass());
        if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
            sb.append(fcda.getLnInst());
        }
        sb.append(".").append(fcda.getDoName());
        if (fcda.getDaName() != null && !fcda.getDaName().isEmpty()) {
            sb.append(".").append(fcda.getDaName());
        }
        return sb.toString();
    }

    /**
     * Builds the full LN name from an FCDA (prefix + lnClass + lnInst).
     * <p>For example: {@code MMXU1} or {@code ALMGGIO1}
     */
    public static String buildLnName(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
            sb.append(fcda.getPrefix());
        }
        sb.append(fcda.getLnClass());
        if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
            sb.append(fcda.getLnInst());
        }
        return sb.toString();
    }

    /**
     * Parses a reference string like {@code C1/MMXU1.Volts} or {@code C1/MMXU1.Volts.mag}
     * into an SclFCDA by matching the LN part against actual LNs in the device.
     *
     * @param server the SCL server model
     * @param ref    the full reference string
     * @return the resolved SclFCDA, or null if the LN cannot be found
     */
    public static SclFCDA parseRefToFcda(SclServer server, String ref) {
        if (ref == null || ref.isEmpty()) return null;

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return null;
        String lnPart = rest.substring(0, dotIdx);
        String doDaPart = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return null;

        SclLN ln = device.findLnByFullName(lnPart);
        if (ln == null) return null;

        SclFCDA fcda = new SclFCDA();
        fcda.setLdInst(ldName);
        fcda.setLnClass(ln.getLnClass());
        fcda.setLnInst(ln.getInst());
        fcda.setPrefix(Objects.requireNonNullElse(ln.getPrefix(), ""));

        int daDotIdx = doDaPart.indexOf('.');
        if (daDotIdx >= 0) {
            fcda.setDoName(doDaPart.substring(0, daDotIdx));
            fcda.setDaName(doDaPart.substring(daDotIdx + 1));
        } else {
            fcda.setDoName(doDaPart);
        }

        return fcda;
    }
}