package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclFCDA {

    private String ldInst;
    private String prefix = "";
    private String lnClass;
    private String lnInst = "";
    private String doName;
    private String daName;
    private String fc;

    /**
     * Builds a full reference string from this FCDA.
     * <p>Format: {@code LDinst/LNFullName.DO[.DA]}
     */
    public String buildFcdaRef() {
        StringBuilder sb = new StringBuilder();
        sb.append(ldInst).append("/");
        if (prefix != null && !prefix.isEmpty()) {
            sb.append(prefix);
        }
        sb.append(lnClass);
        if (lnInst != null && !lnInst.isEmpty()) {
            sb.append(lnInst);
        }
        sb.append(".").append(doName);
        if (daName != null && !daName.isEmpty()) {
            sb.append(".").append(daName);
        }
        return sb.toString();
    }

    /**
     * Builds the full LN name (prefix + lnClass + lnInst) from this FCDA.
     */
    public String buildLnName() {
        StringBuilder sb = new StringBuilder();
        if (prefix != null && !prefix.isEmpty()) {
            sb.append(prefix);
        }
        sb.append(lnClass);
        if (lnInst != null && !lnInst.isEmpty()) {
            sb.append(lnInst);
        }
        return sb.toString();
    }
}
