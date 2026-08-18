package com.ysh.jcms.core.data.scalar;

import com.ysh.jcms.core.data.core.CmsScalar;
import com.ysh.jcms.data.InnerFunctionalConstraint;
import com.ysh.jcms.data.V;

/**
 * <pre>
 * {@code
 * FunctionalConstraint ::= VisibleString (SIZE (2)) — 7.4
 * }
 * </pre>
 *
 * <p>
 * CmsFC stores the value as int (0..12), while inner
 * {@link InnerFunctionalConstraint} stores it as 2-char code ("ST".."XX").
 */
public class CmsFC extends CmsScalar {

    public static final int ST = 0;
    public static final int MX = 1;
    public static final int SP = 2;
    public static final int SV = 3;
    public static final int CF = 4;
    public static final int DC = 5;
    public static final int SG = 6;
    public static final int SE = 7;
    public static final int SR = 8;
    public static final int OR = 9;
    public static final int BL = 10;
    public static final int EX = 11;
    public static final int XX = 12;

    private static final String[] CODES = {"ST", "MX", "SP", "SV", "CF", "DC", "SG", "SE", "SR", "OR", "BL", "EX", "XX"};

    private int val = XX;

    public CmsFC() {
        super(new InnerFunctionalConstraint());
    }
    public CmsFC(int value) {
        this();
        value(value);
    }

    public int value() {
        return val;
    }
    public CmsFC value(int v) {
        if (v < 0 || v > 12)
            throw new IllegalArgumentException("CmsFC out of range [0,12]: " + v);
        this.val = v;
        return this;
    }
    /** Convenience: parse 2-char FC code and set value. */
    public CmsFC value(String v) {
        return value(fromCode(v));
    }

    /**
     * Look up by 2-char FC code ("ST", "MX", ...). Returns XX if unknown.
     */
    public static int fromCode(String code) {
        if (code == null)
            return XX;
        switch (code) {
            case "ST" :
                return ST;
            case "MX" :
                return MX;
            case "SP" :
                return SP;
            case "SV" :
                return SV;
            case "CF" :
                return CF;
            case "DC" :
                return DC;
            case "SG" :
                return SG;
            case "SE" :
                return SE;
            case "SR" :
                return SR;
            case "OR" :
                return OR;
            case "BL" :
                return BL;
            case "EX" :
                return EX;
            case "XX" :
                return XX;
            default :
                throw new IllegalArgumentException("Unknown FC code: " + code);
        }
    }

    /**
     * Case-insensitive variant of {@link #fromCode(String)}.
     */
    public static int fromString(String s) {
        return fromCode(s != null ? s.toUpperCase() : null);
    }

    /**
     * Like {@link #fromCode(String)} but returns the given fallback for unknown
     * codes instead of throwing. SCL 数据里可能含有本实现未定义的 FC（如 IEC 61850 的 CO），
     * 服务端遇到时不应让整个服务崩溃。
     */
    public static int fromCodeOr(String code, int fallback) {
        try {
            return fromCode(code);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    @Override
    public void syncToInner() {
        V.setVal(inner._v, CODES[val]);
    }

    @Override
    public void syncFromInner() {
        Object v = V.getVal(inner._v);
        String s = v instanceof String ? ((String) v).trim() : "";
        if (s.isEmpty()) {
            val = XX;
            return;
        }
        this.val = fromString(s);
    }
}
