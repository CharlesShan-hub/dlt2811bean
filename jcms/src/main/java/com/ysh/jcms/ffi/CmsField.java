package com.ysh.jcms.ffi;

import com.sun.jna.Structure;

/**
 * 字段级基类 — 不可独立编解码，仅作为搭建 PDU 的构件。
 * 所有数据类（CmsBoolean, CmsInt32, CmsQuality 等）应继承此类。
 */
public abstract class CmsField extends CmsType {

    protected CmsField() {
        super(false);
    }

    /**
     * 为当前实例开启 FFI 编解码（供测试用）。
     * 调用后该实例可独立 encode/decode。
     */
    public CmsField test() {
        String cn = getClass().getSimpleName();
        if ("ByValue".equals(cn) && getClass().getEnclosingClass() != null)
            cn = getClass().getEnclosingClass().getSimpleName();
        enableCodec(cn);
        return this;
    }
}
