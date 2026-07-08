package com.ysh.jcms.utils.scl2.ref;

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

    public static SclRef of(String ldName, String lnName, String doName, String daName) {
        String rawRef = ldName + "/" + lnName + "." + doName + "." + daName;
        return new SclRef(ldName, lnName, doName, daName, null, rawRef);
    }

    public static SclRef parse(String ref) {
        return SclRefParser.parse(ref);
    }

    public boolean isDoLevel() {
        return doName != null && daName == null;
    }

    public boolean isDaLevel() {
        return doName != null && daName != null;
    }

    public boolean isLnLevel() {
        return doName == null;
    }

    public boolean hasFc() {
        return fc != null;
    }

    public String getLdName() {
        return ldName;
    }

    public String getLnName() {
        return lnName;
    }

    public String getDoName() {
        return doName;
    }

    public String getDaName() {
        return daName;
    }

    public String getFc() {
        return fc;
    }

    public String getRawRef() {
        return rawRef;
    }
}
