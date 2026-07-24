package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerTriggerConditions;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * TriggerConditions ::= BIT STRING (SIZE(6)) — 7.6.2
 * <p>
 * CmsTriggerConditions stores 5 boolean fields; InnerTriggerConditions packs
 * them as a single int (bit 0 = reserved, always 0).
 */
public class CmsTriggerConditions extends CmsType {

    public CmsBoolean data_change;
    public CmsBoolean quality_change;
    public CmsBoolean data_update;
    public CmsBoolean integrity;
    public CmsBoolean general_interrogation;

    public CmsTriggerConditions() {
        super(new InnerTriggerConditions());
        this.data_change = new CmsBoolean();
        this.quality_change = new CmsBoolean();
        this.data_update = new CmsBoolean();
        this.integrity = new CmsBoolean();
        this.general_interrogation = new CmsBoolean();
    }

    public CmsTriggerConditions data_change(boolean v) { this.data_change.value(v); return this; }
    public CmsTriggerConditions quality_change(boolean v) { this.quality_change.value(v); return this; }
    public CmsTriggerConditions data_update(boolean v) { this.data_update.value(v); return this; }
    public CmsTriggerConditions integrity(boolean v) { this.integrity.value(v); return this; }
    public CmsTriggerConditions general_interrogation(boolean v) { this.general_interrogation.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        if (data_change.value()) packed |= (1 << InnerTriggerConditions.DATA_CHANGE);
        if (quality_change.value()) packed |= (1 << InnerTriggerConditions.QUALITY_CHANGE);
        if (data_update.value()) packed |= (1 << InnerTriggerConditions.DATA_UPDATE);
        if (integrity.value()) packed |= (1 << InnerTriggerConditions.INTEGRITY);
        if (general_interrogation.value()) packed |= (1 << InnerTriggerConditions.GENERAL_INTERROGATION);
        ((InnerTriggerConditions) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerTriggerConditions) inner).value;
        data_change.value((packed >> InnerTriggerConditions.DATA_CHANGE & 1) != 0);
        quality_change.value((packed >> InnerTriggerConditions.QUALITY_CHANGE & 1) != 0);
        data_update.value((packed >> InnerTriggerConditions.DATA_UPDATE & 1) != 0);
        integrity.value((packed >> InnerTriggerConditions.INTEGRITY & 1) != 0);
        general_interrogation.value((packed >> InnerTriggerConditions.GENERAL_INTERROGATION & 1) != 0);
    }
}
