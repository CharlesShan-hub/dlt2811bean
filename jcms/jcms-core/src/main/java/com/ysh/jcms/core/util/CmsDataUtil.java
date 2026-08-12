package com.ysh.jcms.core.util;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.choice.CmsData;

/**
 * Utility for converting {@link CmsData} values to/from string representations.
 * <p>
 * Complex types (quality, utc-time, binary-time) are encoded as hex strings
 * suitable for SCL storage. Basic types use {@link CmsData#toValueString()}.
 */
public final class CmsDataUtil {

    private CmsDataUtil() {
        // utility class, no instantiation
    }

    /**
     * Converts a {@link CmsData} value to its string representation.
     * <ul>
     * <li>Basic types — delegates to {@link CmsData#toValueString()}</li>
     * <li>Complex types (quality, utc-time, binary-time) — synced to inner storage
     * and encoded as hex string</li>
     * </ul>
     *
     * @param data
     *            the CmsData value to convert
     * @return string representation, or {@code null} if conversion fails
     */
    public static String toValueString(CmsData data) {
        int choice = data.choice();
        switch (choice) {
            case CmsData.CHOICE_QUALITY : {
                data.alt_quality.syncToInner();
                Object v = V.getVal(data.alt_quality.inner._v);
                return v instanceof String ? (String) v : null;
            }
            case CmsData.CHOICE_UTC_TIME : {
                data.alt_utc_time.syncToInner();
                Object v = V.getVal(data.alt_utc_time.inner._v);
                return v instanceof byte[] ? InnerBase.hex((byte[]) v) : null;
            }
            case CmsData.CHOICE_BINARY_TIME : {
                data.alt_binary_time.syncToInner();
                Object v = V.getVal(data.alt_binary_time.inner._v);
                return v instanceof byte[] ? InnerBase.hex((byte[]) v) : null;
            }
            default :
                return data.toValueString();
        }
    }
}
