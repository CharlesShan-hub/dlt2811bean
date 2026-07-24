package com.ysh.jcms.data.fc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerFunctionalConstraint;

/**
 * FunctionalConstraint ::= VisibleString (SIZE(2)) — 7.4
 * <p>
 * CmsFC stores the value as int (0..12), while inner
 * {@link InnerFunctionalConstraint} stores it as 2-char code ("ST".."XX").
 */
public class CmsFC extends CmsType {

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

    private static final String[] CODES = {
        "ST", "MX", "SP", "SV", "CF", "DC",
        "SG", "SE", "SR", "OR", "BL", "EX", "XX"
    };

    private int val = XX;

    public CmsFC() {
        super(new InnerFunctionalConstraint());
    }
    public CmsFC(int value) {
        this();
        value(value);
    }

    public int value() { return val; }
    public CmsFC value(int v) {
        if (v < 0 || v > 12)
            throw new IllegalArgumentException("CmsFC out of range [0,12]: " + v);
        this.val = v;
        return this;
    }

    /**
     * Look up by 2-char FC code ("ST", "MX", ...). Returns XX if unknown.
     */
    public static int fromCode(String code) {
        if (code == null) return XX;
        switch (code) {
            case "ST": return ST;
            case "MX": return MX;
            case "SP": return SP;
            case "SV": return SV;
            case "CF": return CF;
            case "DC": return DC;
            case "SG": return SG;
            case "SE": return SE;
            case "SR": return SR;
            case "OR": return OR;
            case "BL": return BL;
            case "EX": return EX;
            case "XX": return XX;
            default: throw new IllegalArgumentException("Unknown FC code: " + code);
        }
    }

    /**
     * Case-insensitive variant of {@link #fromCode(String)}.
     */
    public static int fromString(String s) {
        return fromCode(s != null ? s.toUpperCase() : null);
    }

    @Override
    public void syncToInner() {
        ((InnerFunctionalConstraint) inner).value = CODES[val];
    }

    @Override
    public void syncFromInner() {
        this.val = fromCode(((InnerFunctionalConstraint) inner).value);
    }
}
