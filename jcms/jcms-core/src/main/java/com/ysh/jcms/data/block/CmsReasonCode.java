package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerReasonCode;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * ReasonCode ::= BIT STRING (SIZE(7)) — 7.6.3
 * <p>
 * CmsReasonCode stores 6 boolean fields; InnerReasonCode packs them as a single
 * int (bit 0 = reserved, always 0).
 */
public class CmsReasonCode extends CmsType {

    public CmsBoolean data_change;
    public CmsBoolean quality_change;
    public CmsBoolean data_update;
    public CmsBoolean integrity;
    public CmsBoolean general_interrogation;
    public CmsBoolean application_trigger;

    public CmsReasonCode() {
        super(new InnerReasonCode());
        this.data_change = new CmsBoolean();
        this.quality_change = new CmsBoolean();
        this.data_update = new CmsBoolean();
        this.integrity = new CmsBoolean();
        this.general_interrogation = new CmsBoolean();
        this.application_trigger = new CmsBoolean();
    }

    public CmsReasonCode data_change(boolean v) { this.data_change.value(v); return this; }
    public CmsReasonCode quality_change(boolean v) { this.quality_change.value(v); return this; }
    public CmsReasonCode data_update(boolean v) { this.data_update.value(v); return this; }
    public CmsReasonCode integrity(boolean v) { this.integrity.value(v); return this; }
    public CmsReasonCode general_interrogation(boolean v) { this.general_interrogation.value(v); return this; }
    public CmsReasonCode application_trigger(boolean v) { this.application_trigger.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        if (data_change.value()) packed |= (1 << InnerReasonCode.DATA_CHANGE);
        if (quality_change.value()) packed |= (1 << InnerReasonCode.QUALITY_CHANGE);
        if (data_update.value()) packed |= (1 << InnerReasonCode.DATA_UPDATE);
        if (integrity.value()) packed |= (1 << InnerReasonCode.INTEGRITY);
        if (general_interrogation.value()) packed |= (1 << InnerReasonCode.GENERAL_INTERROGATION);
        if (application_trigger.value()) packed |= (1 << InnerReasonCode.APPLICATION_TRIGGER);
        ((InnerReasonCode) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerReasonCode) inner).value;
        data_change.value((packed >> InnerReasonCode.DATA_CHANGE & 1) != 0);
        quality_change.value((packed >> InnerReasonCode.QUALITY_CHANGE & 1) != 0);
        data_update.value((packed >> InnerReasonCode.DATA_UPDATE & 1) != 0);
        integrity.value((packed >> InnerReasonCode.INTEGRITY & 1) != 0);
        general_interrogation.value((packed >> InnerReasonCode.GENERAL_INTERROGATION & 1) != 0);
        application_trigger.value((packed >> InnerReasonCode.APPLICATION_TRIGGER & 1) != 0);
    }
}
