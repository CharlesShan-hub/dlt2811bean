package com.ysh.jcms.util;

import com.ysh.jcms.datatype.choice.CmsData;
import com.ysh.jcms.datatype.choice.CmsDataType;
import com.ysh.jcms.ffi.CmsType;

/**
 * CmsData 专用格式化器 — 根据 choice 只展开活跃值，不显示所有 union 字段。
 */
public class CmsDataFormatter implements CmsToString {

    public static final CmsDataFormatter INSTANCE = new CmsDataFormatter();

    @Override
    public String toString(CmsType obj, int indent) {
        CmsData d = (CmsData) obj;
        String pad = indent > 0 ? CmsDefaultFormatter.repeat("    ", indent) : "";
        int c = d.choice.value();
        Object active = d.value.get(c);
        String valStr = (active instanceof CmsType)
            ? CmsDefaultFormatter.formatValue(active, indent + 1)
            : String.valueOf(active);
        return "(" + d.getClass().getSimpleName() + ") {\n"
            + pad + "    choice: (" + CmsDataType.class.getSimpleName() + ") " + c + "\n"
            + pad + "    value: (CmsDataUnion) -> " + valStr + "\n"
            + pad + "}";
    }
}
