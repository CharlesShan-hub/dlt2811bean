package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsDbpos;
import com.ysh.jcms.data.common.CmsTcmd;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsBitString;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * bType → CMS 协议类型 映射器。
 * <p>
 * 纯查表，将 bType 字符串和值转换为对应的 {@link CmsTypeOld} 实例。
 */
public final class TypeMapper {

    private TypeMapper() {
    }

    /**
     * 将 bType + 字符串值转换为对应的 CmsType 实例。
     *
     * @param bType
     *            SCL bType，如 "INT32"、"FLOAT32"、"BOOLEAN"
     * @param value
     *            字符串值
     * @return CmsType 实例，解析失败返回 CmsUint8Array 兜底
     */
    public static CmsTypeOld createTypedValue(String bType, String value) {
        if (bType == null || value == null) {
            return new CmsUint8Array(value != null ? value : "");
        }
        try {
            String v = value.trim();
            switch (bType) {
                case "BOOLEAN" :
                case "BOOL" :
                    return new CmsBoolean(Boolean.parseBoolean(v));
                case "INT8" :
                    return new CmsInt8(Integer.parseInt(v));
                case "INT16" :
                    return new CmsInt16(Integer.parseInt(v));
                case "INT32" :
                    return new CmsInt32(Integer.parseInt(v));
                case "INT64" :
                    return new CmsInt64(Long.parseLong(v));
                case "INT8U" :
                    return new CmsInt8U(Integer.parseInt(v));
                case "INT16U" :
                    return new CmsInt16U(Integer.parseInt(v));
                case "INT32U" :
                    return new CmsInt32U(Long.parseLong(v));
                case "INT64U" :
                    return new CmsInt64U(new java.math.BigInteger(v));
                case "FLOAT32" :
                    return new CmsFloat32(Float.parseFloat(v));
                case "FLOAT64" :
                    return new CmsFloat64(Double.parseDouble(v));
                case "Enum" :
                    return new CmsInt32(Integer.parseInt(v));
                case "Dbpos" :
                    return new CmsDbpos(Integer.parseInt(v));
                case "Tcmd" :
                    return new CmsTcmd(Integer.parseInt(v));
                case "VisString255" :
                case "VISIBLE STRING" :
                    return new CmsUint8Array(value, CmsUint8Array.TYPE_VISIBLE_STRING);
                case "Unicode255" :
                case "UNICODE STRING" :
                    return new CmsUint8Array(value, CmsUint8Array.TYPE_UNICODE_STRING);
                case "BIT_STRING" :
                case "BITSTRING" :
                    return new CmsBitString(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                case "Check" :
                    return new CmsCheck(Integer.parseInt(v));
                default :
                    return new CmsUint8Array(value, CmsUint8Array.TYPE_UNKNOWN);
            }
        } catch (Exception e) {
            return new CmsUint8Array(value, CmsUint8Array.TYPE_UNKNOWN);
        }
    }
}
