package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.ref.SclRef;

/**
 * DataSet/FCDA reference converter.
 * <p>
 * Bidirectional conversion between FCDA and reference string, based on {@link SclRef} and {@link Navigator}.
 */
public final class DataSetResolver {

    private DataSetResolver() {
    }

    /**
     * Builds a complete reference string from an FCDA.
     * <p>
     * Format: {@code LD/LN.DO[.DA]}, e.g. {@code C1/MMXU1.Volts.mag}
     */
    public static String fcdaRef(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        sb.append(fcda.ldInst()).append("/");
        String p = fcda.prefix();
        if (p != null && !p.isEmpty())
            sb.append(p);
        sb.append(fcda.lnClass());
        String i = fcda.lnInst();
        if (i != null && !i.isEmpty())
            sb.append(i);
        sb.append(".").append(fcda.doName());
        String d = fcda.daName();
        if (d != null && !d.isEmpty())
            sb.append(".").append(d);
        return sb.toString();
    }

    /**
     * Builds the full LN name (prefix + lnClass + lnInst) from an FCDA.
     */
    public static String fcdaLnName(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        String p = fcda.prefix();
        if (p != null && !p.isEmpty())
            sb.append(p);
        sb.append(fcda.lnClass());
        String i = fcda.lnInst();
        if (i != null && !i.isEmpty())
            sb.append(i);
        return sb.toString();
    }

    /**
     * Parses an FCDA object from a reference string.
     * <p>
     * Locates the LN via Navigator, extracts lnClass/lnInst/prefix and fills them into the FCDA.
     */
    public static SclFCDA parseRef(Navigator nav) {
        if (!nav.isValid() || nav.ln() == null)
            return null;

        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(nav.ref().ldInst());
        fcda.lnClass(nav.ln().lnClass());
        fcda.lnInst(nav.ln().inst());
        fcda.prefix(nav.ln().prefix() != null ? nav.ln().prefix() : "");
        fcda.doName(nav.ref().doName());
        fcda.daName(nav.ref().daName());
        return fcda;
    }
}
