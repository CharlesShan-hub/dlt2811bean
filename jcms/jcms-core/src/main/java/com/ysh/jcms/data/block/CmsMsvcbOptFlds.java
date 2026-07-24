package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerMsvcbOptFlds;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * MsvcbOptFlds ::= BIT STRING (SIZE(5)) — 7.6.6
 * <p>
 * CmsMsvcbOptFlds stores 4 boolean fields; InnerMsvcbOptFlds packs them as a
 * single int (bit 1 = reserved, always 0).
 */
public class CmsMsvcbOptFlds extends CmsType {

    public CmsBoolean refresh_time;
    public CmsBoolean sample_rate;
    public CmsBoolean data_set_name;
    public CmsBoolean security;

    public CmsMsvcbOptFlds() {
        super(new InnerMsvcbOptFlds());
        this.refresh_time = new CmsBoolean();
        this.sample_rate = new CmsBoolean();
        this.data_set_name = new CmsBoolean();
        this.security = new CmsBoolean();
    }

    public CmsMsvcbOptFlds refresh_time(boolean v) { this.refresh_time.value(v); return this; }
    public CmsMsvcbOptFlds sample_rate(boolean v) { this.sample_rate.value(v); return this; }
    public CmsMsvcbOptFlds data_set_name(boolean v) { this.data_set_name.value(v); return this; }
    public CmsMsvcbOptFlds security(boolean v) { this.security.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        if (refresh_time.value()) packed |= (1 << 0);
        if (sample_rate.value()) packed |= (1 << 2);
        if (data_set_name.value()) packed |= (1 << 3);
        if (security.value()) packed |= (1 << 4);
        ((InnerMsvcbOptFlds) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerMsvcbOptFlds) inner).value;
        refresh_time.value((packed & (1 << 0)) != 0);
        sample_rate.value((packed & (1 << 2)) != 0);
        data_set_name.value((packed & (1 << 3)) != 0);
        security.value((packed & (1 << 4)) != 0);
    }
}
