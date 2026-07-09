package com.ysh.jcms.utils.scl.ref;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SCL 引用 —— 模型中的通用索引键。
 * <p>
 * 引用格式：{@code [IEDName/]LDInst/LNName[.DO][[.SDI]...][.DA][FC]}
 * <br>
 * 例如：{@code E1Q1SB1/C1/MMXU1.Volts.sVC.offset}
 */
@Getter
@Accessors(fluent = true)
public class SclRef {

    private final String iedName;
    private final String ldInst;
    private final String lnName;
    private final String doName;
    private final List<String> sdiChain;
    private final String daName;
    private final String fc;

    public SclRef(String iedName, String ldInst, String lnName,
                  String doName, List<String> sdiChain, String daName, String fc) {
        this.iedName = iedName;
        this.ldInst = ldInst;
        this.lnName = lnName;
        this.doName = doName;
        this.sdiChain = sdiChain != null ? Collections.unmodifiableList(sdiChain) : Collections.emptyList();
        this.daName = daName;
        this.fc = fc;
    }

    // ==================== 工厂方法 ====================

    public static SclRefBuilder ld(String ldInst) {
        return new SclRefBuilder(ldInst);
    }

    public static SclRef parse(String ref) {
        return SclRefParser.parse(ref);
    }

    /** Builder */
    public static class SclRefBuilder {
        private String iedName;
        private final String ldInst;
        private String prefix = "";
        private String lnClass;
        private String lnInst = "";
        private String doName;
        private final java.util.ArrayList<String> sdiChain = new java.util.ArrayList<>();
        private String daName;
        private String fc;

        SclRefBuilder(String ldInst) { this.ldInst = ldInst; }

        public SclRefBuilder ied(String iedName) { this.iedName = iedName; return this; }
        public SclRefBuilder prefix(String prefix) { this.prefix = prefix; return this; }
        public SclRefBuilder lnClass(String lnClass) { this.lnClass = lnClass; return this; }
        public SclRefBuilder lnInst(String lnInst) { this.lnInst = lnInst; return this; }
        public SclRefBuilder doName(String doName) { this.doName = doName; return this; }
        public SclRefBuilder addSdi(String sdiName) { this.sdiChain.add(sdiName); return this; }
        public SclRefBuilder daName(String daName) { this.daName = daName; return this; }
        public SclRefBuilder fc(String fc) { this.fc = fc; return this; }

        public SclRef build() {
            String ln = (prefix != null ? prefix : "")
                      + (lnClass != null ? lnClass : "")
                      + (lnInst != null ? lnInst : "");
            return new SclRef(iedName, ldInst, ln, doName,
                    sdiChain.isEmpty() ? null : sdiChain, daName, fc);
        }

        public SclRefBuilder lnName(String lnName) {
            this.prefix = "";
            this.lnClass = lnName;
            this.lnInst = "";
            return this;
        }
    }

    // ==================== 层级判断 ====================

    public boolean isLnLevel() { return doName == null; }
    public boolean isDoLevel() { return doName != null && daName == null; }
    public boolean isDaLevel() { return doName != null && daName != null; }
    public boolean hasSdi() { return !sdiChain.isEmpty(); }
    public boolean hasFc() { return fc != null && !fc.isEmpty(); }

    /** @deprecated 使用 {@link #ldInst()} */
    @Deprecated
    public String ldName() { return ldInst; }

    // ==================== 引用组合 ====================

    public String lnReference() { return ldInst + "/" + lnName; }
    public String doReference() { return ldInst + "/" + lnName + "." + doName; }
    public String daReference() {
        StringBuilder sb = new StringBuilder(ldInst).append("/").append(lnName).append(".").append(doName);
        for (String sdi : sdiChain) sb.append(".").append(sdi);
        if (daName != null) sb.append(".").append(daName);
        return sb.toString();
    }

    public String fullReference() {
        StringBuilder sb = new StringBuilder();
        if (iedName != null) sb.append(iedName).append("/");
        sb.append(ldInst).append("/").append(lnName);
        if (doName != null) {
            sb.append(".").append(doName);
            for (String sdi : sdiChain) sb.append(".").append(sdi);
            if (daName != null) sb.append(".").append(daName);
        }
        if (fc != null) sb.append("[").append(fc).append("]");
        return sb.toString();
    }

    @Override
    public String toString() { return fullReference(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SclRef)) return false;
        SclRef other = (SclRef) o;
        return Objects.equals(iedName, other.iedName)
                && Objects.equals(ldInst, other.ldInst)
                && Objects.equals(lnName, other.lnName)
                && Objects.equals(doName, other.doName)
                && Objects.equals(sdiChain, other.sdiChain)
                && Objects.equals(daName, other.daName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iedName, ldInst, lnName, doName, sdiChain, daName);
    }
}
