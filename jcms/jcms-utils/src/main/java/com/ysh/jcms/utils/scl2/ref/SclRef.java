package com.ysh.jcms.utils.scl2.ref;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * SCL 引用 —— 表示一个结构化的数据引用路径。
 * <p>
 * 由 {@link SclRefParser#parse(String)} 生成，不可变。
 */
@Getter
@Accessors(fluent = true)
public class SclRef {

    private final String ldName;
    private final String lnName;
    private final String doName;
    private final String daName;
    private final String fc;
    private final String rawRef;

    public SclRef(String ldName, String lnName, String doName, String daName, String fc, String rawRef) {
        this.ldName = ldName;
        this.lnName = lnName;
        this.doName = doName;
        this.daName = daName;
        this.fc = fc;
        this.rawRef = rawRef;
    }

    // ==================== 工厂方法 ====================

    public static SclRef of(String ldName, String lnName, String doName, String daName) {
        StringBuilder sb = new StringBuilder();
        sb.append(ldName).append("/").append(lnName);
        if (doName != null && !doName.isEmpty()) {
            sb.append(".").append(doName);
            if (daName != null && !daName.isEmpty()) {
                sb.append(".").append(daName);
            }
        }
        return new SclRef(ldName, lnName, doName, daName, null, sb.toString());
    }

    public static SclRef parse(String ref) {
        return SclRefParser.parse(ref);
    }

    // ==================== 层级判断 ====================

    public boolean isLnLevel() {
        return doName == null;
    }

    public boolean isDoLevel() {
        return doName != null && daName == null;
    }

    public boolean isDaLevel() {
        return doName != null && daName != null;
    }

    public boolean hasFc() {
        return fc != null && !fc.isEmpty();
    }

    // ==================== 引用组合 ====================

    public String lnReference() {
        return ldName + "/" + lnName;
    }

    public String doReference() {
        return ldName + "/" + lnName + "." + doName;
    }

    public String daReference() {
        return ldName + "/" + lnName + "." + doName + "." + daName;
    }

    public String fullReference() {
        return rawRef;
    }

    // ==================== equals / hashCode / toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SclRef)) return false;
        SclRef other = (SclRef) o;
        return Objects.equals(ldName, other.ldName)
                && Objects.equals(lnName, other.lnName)
                && Objects.equals(doName, other.doName)
                && Objects.equals(daName, other.daName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ldName, lnName, doName, daName);
    }

    @Override
    public String toString() {
        return rawRef;
    }
}
