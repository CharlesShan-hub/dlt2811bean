package com.ysh.jcms.datatype.choice;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.*;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Tagged union choice discriminator for Data / DataDefinition (0–23).
 * 也承载所有 choice ↔ 类型/字段的映射关系。
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsDataType extends CmsScalar {
    // ==================== Choice 常量 ====================

    public static final int ERROR          = 0;
    public static final int ARRAY          = 1;
    public static final int STRUCTURE      = 2;
    public static final int BOOLEAN        = 3;
    public static final int INT8           = 4;
    public static final int INT16          = 5;
    public static final int INT32          = 6;
    public static final int INT64          = 7;
    public static final int INT8U          = 8;
    public static final int INT16U         = 9;
    public static final int INT32U         = 10;
    public static final int INT64U         = 11;
    public static final int FLOAT32        = 12;
    public static final int FLOAT64        = 13;
    public static final int BIT_STRING     = 14;
    public static final int OCTET_STRING   = 15;
    public static final int VISIBLE_STRING = 16;
    public static final int UTF8_STRING    = 17;
    public static final int UTC_TIME       = 18;
    public static final int BINARY_TIME    = 19;
    public static final int QUALITY        = 20;
    public static final int DBPOS          = 21;
    public static final int TCMD           = 22;
    public static final int CHECK          = 23;

    // ==================== 映射表 ====================

    /** choice → 基类类型（用于类型推断）。 */
    private static final Class<?>[] BASE_TYPES = {
        CmsServiceError.class,         //  0
        CmsDataArray.class,            //  1
        CmsDataStructure.class,        //  2
        CmsBoolean.class,              //  3
        CmsInt8.class,                 //  4
        CmsInt16.class,                //  5
        CmsInt32.class,                //  6
        CmsInt64.class,                //  7
        CmsInt8U.class,                //  8
        CmsInt16U.class,               //  9
        CmsInt32U.class,               // 10
        CmsInt64U.class,               // 11
        CmsFloat32.class,              // 12
        CmsFloat64.class,              // 13
        CmsUint8Array.class,           // 14
        CmsUint8Array.class,           // 15
        CmsUint8Array.class,           // 16
        CmsUint8Array.class,           // 17
        CmsUtcTime.class,              // 18
        CmsBinaryTime.class,           // 19
        CmsQuality.class,              // 20
        CmsDbpos.class,                // 21
        CmsTcmd.class,                 // 22
        CmsCheck.class,                // 23
    };

    /** choice → Union 字段的 ByValue 类型。 */
    private static final Class<?>[] BY_VALUE_TYPES = {
        CmsServiceError.ByValue.class,        //  0
        CmsDataArray.ByValue.class,           //  1
        CmsDataStructure.ByValue.class,       //  2
        CmsBoolean.ByValue.class,             //  3
        CmsInt8.ByValue.class,                //  4
        CmsInt16.ByValue.class,               //  5
        CmsInt32.ByValue.class,               //  6
        CmsInt64.ByValue.class,               //  7
        CmsInt8U.ByValue.class,               //  8
        CmsInt16U.ByValue.class,              //  9
        CmsInt32U.ByValue.class,              // 10
        CmsInt64U.ByValue.class,              // 11
        CmsFloat32.ByValue.class,             // 12
        CmsFloat64.ByValue.class,             // 13
        CmsUint8Array.ByValue.class,          // 14
        CmsUint8Array.ByValue.class,          // 15
        CmsUint8Array.ByValue.class,          // 16
        CmsUint8Array.ByValue.class,          // 17
        CmsUtcTime.ByValue.class,             // 18
        CmsBinaryTime.ByValue.class,          // 19
        CmsQuality.ByValue.class,             // 20
        CmsDbpos.ByValue.class,               // 21
        CmsTcmd.ByValue.class,                // 22
        CmsCheck.ByValue.class,               // 23
    };

    // ==================== 查询方法 ====================

    /** choice → Union 字段的 ByValue 类型。 */
    public static Class<?> unionClass(int c) {
        if (c >= 0 && c < BY_VALUE_TYPES.length) return BY_VALUE_TYPES[c];
        return CmsInt32.ByValue.class;
    }

    /** 从值类型推断 choice 编号。 */
    public static int choiceFor(Class<?> cls) {
        if ("ByValue".equals(cls.getSimpleName()) && cls.getEnclosingClass() != null)
            cls = cls.getEnclosingClass();
        for (int i = 0; i < BASE_TYPES.length; i++) {
            if (BASE_TYPES[i].isAssignableFrom(cls)) return i;
        }
        return -1;
    }

    // ==================== 字段 ====================

    public int value;

    public CmsDataType() {
        super(false);  // 纯包装类，无 FFI 绑定
    }

    public static class ByValue extends CmsDataType implements Structure.ByValue {}
}
