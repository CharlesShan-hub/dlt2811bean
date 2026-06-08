package com.ysh.jcms.ffi;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * PDU 级基类 — 可独立编解码，内置 APDU 头部 reqId。
 * 所有 service 层 PDU 应继承此类。
 *
 * 字段布局（PER编码顺序）：
 *   1. reqId — 请求标识
 *   2. 子类具体内容
 */
@Getter
@Setter
@Accessors(fluent = true)
public abstract class CmsAPDU extends CmsType {

    /** 请求标识。 */
    public CmsInt32U reqId = new CmsInt32U();

    protected CmsAPDU() {
        super(true);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("reqId");
    }
}
