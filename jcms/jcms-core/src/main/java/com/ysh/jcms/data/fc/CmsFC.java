package com.ysh.jcms.data.fc;

import com.ysh.jcms.core.CmsEnumerated;

/**
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 *
 * PER wire format is VisibleString(SIZE(2)) — 2 ASCII chars.
 * Use {@link #fromCode(String)} to lookup an FC value by its 2-char code
 * (e.g. "ST" → 0), or {@link #fromString(String)} for case-insensitive lookup.
 */
public class CmsFC extends CmsEnumerated {

    public static final int ST  = 0;
    public static final int MX  = 1;
    public static final int SP  = 2;
    public static final int SV  = 3;
    public static final int CF  = 4;
    public static final int DC  = 5;
    public static final int SG  = 6;
    public static final int SE  = 7;
    public static final int SR  = 8;
    public static final int OR  = 9;
    public static final int BL  = 10;
    public static final int EX  = 11;
    public static final int XX  = 12;

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
            default:   return XX;
        }
    }

    /**
     * Case-insensitive variant of {@link #fromCode(String)}.
     */
    public static int fromString(String s) {
        return fromCode(s != null ? s.toUpperCase() : null);
    }

    public CmsFC() { super(0, 12, ST); }

    public CmsFC(int value) { super(0, 12, value); }

    @Override
    public CmsFC value(int v) { return (CmsFC) super.value(v); }
}
