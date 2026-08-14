package com.ysh.jcms.utils.scl.navigate;

/**
 * bType → CMS protocol type mapping table.
 * <p>
 * Pure lookup table, zero state. Responsible for two things:
 * <ol>
 * <li>bType → selector value of CmsDataDefinition.choice</li>
 * <li>bType → CmsType mapping</li>
 * </ol>
 */
public final class CmsDataTypeMap {

    // Data definition selectors (alternatives 0..23)
    public static final int SEL_ERROR = 0;
    public static final int SEL_ARRAY = 1;
    public static final int SEL_STRUCTURE = 2;
    public static final int SEL_BOOLEAN = 3;
    public static final int SEL_INT8 = 4;
    public static final int SEL_INT16 = 5;
    public static final int SEL_INT32 = 6;
    public static final int SEL_INT64 = 7;
    public static final int SEL_INT8U = 8;
    public static final int SEL_INT16U = 9;
    public static final int SEL_INT32U = 10;
    public static final int SEL_INT64U = 11;
    public static final int SEL_FLOAT32 = 12;
    public static final int SEL_FLOAT64 = 13;
    public static final int SEL_BIT_STRING = 14;
    public static final int SEL_VISIBLE_STRING = 16;
    public static final int SEL_UNICODE_STRING = 17;
    public static final int SEL_QUALITY = 18;
    public static final int SEL_UTC_TIME = 19;
    public static final int SEL_BINARY_TIME = 20;
    public static final int SEL_DBPOS = 21;
    public static final int SEL_TCMD = 22;
    public static final int SEL_CHECK = 23;

    private CmsDataTypeMap() {
    }

    /**
     * Maps a bType string to the CHOICE selector value of CmsDataDefinition.
     * Returns SEL_BOOLEAN (default fallback) when unrecognized.
     */
    public static int toSelector(String bType) {
        if (bType == null)
            return SEL_BOOLEAN;
        switch (bType.toUpperCase()) {
            case "BOOLEAN" :
                return SEL_BOOLEAN;
            case "INT8" :
                return SEL_INT8;
            case "INT16" :
                return SEL_INT16;
            case "INT32" :
                return SEL_INT32;
            case "INT64" :
                return SEL_INT64;
            case "INT8U" :
                return SEL_INT8U;
            case "INT16U" :
                return SEL_INT16U;
            case "INT32U" :
                return SEL_INT32U;
            case "INT64U" :
                return SEL_INT64U;
            case "FLOAT32" :
                return SEL_FLOAT32;
            case "FLOAT64" :
                return SEL_FLOAT64;
            case "BIT_STRING" :
            case "BITSTRING" :
                return SEL_BIT_STRING;
            case "OCTET_STRING" :
            case "OCTETSTRING" :
            case "VISSTRING255" :
            case "VISSTRING64" :
            case "VISIBLE_STRING" :
                return SEL_VISIBLE_STRING;
            case "UNICODE_STRING" :
            case "UNICODESTRING" :
            case "UNICODE255" :
                return SEL_UNICODE_STRING;
            case "UTC_TIME" :
            case "UTCTIME" :
            case "TIMESTAMP" :
                return SEL_UTC_TIME;
            case "BINARY_TIME" :
            case "BINARYTIME" :
            case "ENTRYTIME" :
                return SEL_BINARY_TIME;
            case "QUALITY" :
                return SEL_QUALITY;
            case "DBPOS" :
                return SEL_DBPOS;
            case "TCMD" :
                return SEL_TCMD;
            case "CHECK" :
                return SEL_CHECK;
            case "STRUCT" :
                return SEL_STRUCTURE;
            default :
                return SEL_BOOLEAN;
        }
    }

    /**
     * Gets the length constraint of VisibleString. VisString255 → 255, VisString64
     * → 64, others → 0
     */
    public static int visibleStringLength(String bType) {
        if (bType == null)
            return 0;
        switch (bType.toUpperCase()) {
            case "VISSTRING255" :
            case "VISIBLE_STRING" :
                return 255;
            case "VISSTRING64" :
                return 64;
            default :
                return 0;
        }
    }
}
