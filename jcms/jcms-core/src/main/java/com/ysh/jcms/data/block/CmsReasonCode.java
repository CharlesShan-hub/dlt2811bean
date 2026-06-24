package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * ReasonCode ::= BIT STRING (SIZE(7))  —  7.6.3
 * PER: align + 1 byte (7 bits)
 *
 * All-pointer container:
 *   [0] data_change
 *   [8] quality_change
 *   [16] data_update
 *   [24] integrity
 *   [32] general_interrogation
 *   [40] application_trigger
 */
public class CmsReasonCode extends CmsType {

    public CmsBoolean data_change;
    public CmsBoolean quality_change;
    public CmsBoolean data_update;
    public CmsBoolean integrity;
    public CmsBoolean general_interrogation;
    public CmsBoolean application_trigger;

    public CmsReasonCode() { super(Codec.REASON_CODE);
        this.data_change           = new CmsBoolean();
        this.quality_change        = new CmsBoolean();
        this.data_update           = new CmsBoolean();
        this.integrity             = new CmsBoolean();
        this.general_interrogation = new CmsBoolean();
        this.application_trigger   = new CmsBoolean();
    }
    
    public CmsReasonCode data_change(boolean v) { this.data_change.value(v); return this; }
    public CmsReasonCode quality_change(boolean v) { this.quality_change.value(v); return this; }
    public CmsReasonCode data_update(boolean v) { this.data_update.value(v); return this; }
    public CmsReasonCode integrity(boolean v) { this.integrity.value(v); return this; }
    public CmsReasonCode general_interrogation(boolean v) { this.general_interrogation.value(v); return this; }
    public CmsReasonCode application_trigger(boolean v) { this.application_trigger.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(data_change, quality_change, data_update,
                             integrity, general_interrogation, application_trigger);
    }
}