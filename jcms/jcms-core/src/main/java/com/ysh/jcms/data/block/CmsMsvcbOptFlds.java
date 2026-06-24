package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * MsvcbOptFlds ::= BIT STRING (SIZE(5))  —  7.6.6
 * PER: align + 1 byte (5 bits)
 *
 * All-pointer container:
 *   [0]  refresh_time
 *   [8]  sample_rate
 *   [16] data_set_name
 *   [24] security
 */
public class CmsMsvcbOptFlds extends CmsType {

    public CmsBoolean refresh_time;
    public CmsBoolean sample_rate;
    public CmsBoolean data_set_name;
    public CmsBoolean security;

    public CmsMsvcbOptFlds() { super(Codec.MSVCB_OPT_FLDS);
        this.refresh_time  = new CmsBoolean();
        this.sample_rate   = new CmsBoolean();
        this.data_set_name = new CmsBoolean();
        this.security      = new CmsBoolean();
    }
    
    public CmsMsvcbOptFlds refresh_time(boolean v) { this.refresh_time.value(v); return this; }
    public CmsMsvcbOptFlds sample_rate(boolean v) { this.sample_rate.value(v); return this; }
    public CmsMsvcbOptFlds data_set_name(boolean v) { this.data_set_name.value(v); return this; }
    public CmsMsvcbOptFlds security(boolean v) { this.security.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(refresh_time, sample_rate, data_set_name, security);
    }
}