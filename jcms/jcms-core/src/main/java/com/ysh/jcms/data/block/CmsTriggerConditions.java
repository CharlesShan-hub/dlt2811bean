package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * TriggerConditions ::= BIT STRING (SIZE(6))  —  7.6.2
 * PER: align + 1 byte (6 bits)
 *
 * All-pointer container:
 *   [0] data_change
 *   [8] quality_change
 *   [16] data_update
 *   [24] integrity
 *   [32] general_interrogation
 */
public class CmsTriggerConditions extends CmsType {

    public CmsBoolean data_change;
    public CmsBoolean quality_change;
    public CmsBoolean data_update;
    public CmsBoolean integrity;
    public CmsBoolean general_interrogation;

    public CmsTriggerConditions() { super(Codec.TRIGGER_CONDITIONS);
        this.data_change           = new CmsBoolean();
        this.quality_change        = new CmsBoolean();
        this.data_update           = new CmsBoolean();
        this.integrity             = new CmsBoolean();
        this.general_interrogation = new CmsBoolean();
    }
    
    public CmsTriggerConditions data_change(boolean v) { this.data_change.value(v); return this; }
    public CmsTriggerConditions quality_change(boolean v) { this.quality_change.value(v); return this; }
    public CmsTriggerConditions data_update(boolean v) { this.data_update.value(v); return this; }
    public CmsTriggerConditions integrity(boolean v) { this.integrity.value(v); return this; }
    public CmsTriggerConditions general_interrogation(boolean v) { this.general_interrogation.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(data_change, quality_change, data_update,
                             integrity, general_interrogation);
    }
}