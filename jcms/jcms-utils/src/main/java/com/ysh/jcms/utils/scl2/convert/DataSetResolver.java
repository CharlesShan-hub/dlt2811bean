package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.utils.scl2.model.input.SclFCDA;
import com.ysh.jcms.utils.scl2.navigate.Navigator;
import com.ysh.jcms.utils.scl2.ref.SclRef;

/**
 * DataSet/FCDA 引用转换器。
 * <p>
 * FCDA ↔ 引用字符串 互转，基于 {@link SclRef} 和 {@link Navigator}。
 */
public final class DataSetResolver {

    private DataSetResolver() {}

    /**
     * 将 FCDA 构建为完整引用字符串。
     * <p>格式：{@code LD/LN.DO[.DA]}，例如 {@code C1/MMXU1.Volts.mag}
     */
    public static String fcdaRef(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        sb.append(fcda.ldInst()).append("/");
        String p = fcda.prefix();
        if (p != null && !p.isEmpty()) sb.append(p);
        sb.append(fcda.lnClass());
        String i = fcda.lnInst();
        if (i != null && !i.isEmpty()) sb.append(i);
        sb.append(".").append(fcda.doName());
        String d = fcda.daName();
        if (d != null && !d.isEmpty()) sb.append(".").append(d);
        return sb.toString();
    }

    /**
     * 从 FCDA 构建 LN 全名（prefix + lnClass + lnInst）。
     */
    public static String fcdaLnName(SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        String p = fcda.prefix();
        if (p != null && !p.isEmpty()) sb.append(p);
        sb.append(fcda.lnClass());
        String i = fcda.lnInst();
        if (i != null && !i.isEmpty()) sb.append(i);
        return sb.toString();
    }

    /**
     * 从引用字符串解析为 FCDA 对象。
     * <p>通过 Navigator 定位到 LN，提取 lnClass/lnInst/prefix 填入 FCDA。
     */
    public static SclFCDA parseRef(Navigator nav) {
        if (!nav.isValid() || nav.ln() == null) return null;

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
