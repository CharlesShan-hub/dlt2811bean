package com.ysh.jcms.utils.scl.ref;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SclRef {

    private final String ldName;
    private final String lnName;
    private final String doName;
    private final String daName;
    private final String fc;
    private final String rawRef;

    public boolean hasDa() { return daName != null && !daName.isEmpty(); }

    public boolean hasDo() { return doName != null && !doName.isEmpty(); }

    public boolean hasFc() { return fc != null && !fc.isEmpty(); }

    public boolean isDaLevel() { return hasDa(); }

    public boolean isDoLevel() { return hasDo() && !hasDa(); }

    public boolean isLnLevel() { return !hasDo(); }

    public String getLnReference() {
        return ldName + "/" + lnName;
    }

    public String getDoReference() {
        return ldName + "/" + lnName + "." + doName;
    }

    public String getDaReference() {
        return ldName + "/" + lnName + "." + doName + "." + daName;
    }

    public String getFullReference() {
        return rawRef;
    }

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

    public static SclRef parse(String ref) {
        return SclRefParser.parse(ref);
    }

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
}
