package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerRcbOptFlds;
import com.ysh.jcms.data.scalar.CmsBoolean;

/**
 * RcbOptFlds ::= BIT STRING (SIZE(10)) — 7.6.4
 * <p>
 * CmsRcbOptFlds stores 9 boolean fields; InnerRcbOptFlds packs them as a
 * single int (bit 0 = reserved, always 0).
 */
public class CmsRcbOptFlds extends CmsType {

    public CmsBoolean sequence_number;
    public CmsBoolean report_time_stamp;
    public CmsBoolean reason_for_inclusion;
    public CmsBoolean data_set_name;
    public CmsBoolean data_reference;
    public CmsBoolean buffer_overflow;
    public CmsBoolean entry_id;
    public CmsBoolean conf_revision;
    public CmsBoolean segmentation;

    public CmsRcbOptFlds() {
        super(new InnerRcbOptFlds());
        this.sequence_number = new CmsBoolean();
        this.report_time_stamp = new CmsBoolean();
        this.reason_for_inclusion = new CmsBoolean();
        this.data_set_name = new CmsBoolean();
        this.data_reference = new CmsBoolean();
        this.buffer_overflow = new CmsBoolean();
        this.entry_id = new CmsBoolean();
        this.conf_revision = new CmsBoolean();
        this.segmentation = new CmsBoolean();
    }

    public CmsRcbOptFlds sequence_number(boolean v) { this.sequence_number.value(v); return this; }
    public CmsRcbOptFlds report_time_stamp(boolean v) { this.report_time_stamp.value(v); return this; }
    public CmsRcbOptFlds reason_for_inclusion(boolean v) { this.reason_for_inclusion.value(v); return this; }
    public CmsRcbOptFlds data_set_name(boolean v) { this.data_set_name.value(v); return this; }
    public CmsRcbOptFlds data_reference(boolean v) { this.data_reference.value(v); return this; }
    public CmsRcbOptFlds buffer_overflow(boolean v) { this.buffer_overflow.value(v); return this; }
    public CmsRcbOptFlds entry_id(boolean v) { this.entry_id.value(v); return this; }
    public CmsRcbOptFlds conf_revision(boolean v) { this.conf_revision.value(v); return this; }
    public CmsRcbOptFlds segmentation(boolean v) { this.segmentation.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        if (sequence_number.value()) packed |= (1 << InnerRcbOptFlds.SEQUENCE_NUMBER);
        if (report_time_stamp.value()) packed |= (1 << InnerRcbOptFlds.REPORT_TIME_STAMP);
        if (reason_for_inclusion.value()) packed |= (1 << InnerRcbOptFlds.REASON_FOR_INCLUSION);
        if (data_set_name.value()) packed |= (1 << InnerRcbOptFlds.DATA_SET_NAME);
        if (data_reference.value()) packed |= (1 << InnerRcbOptFlds.DATA_REFERENCE);
        if (buffer_overflow.value()) packed |= (1 << InnerRcbOptFlds.BUFFER_OVERFLOW);
        if (entry_id.value()) packed |= (1 << InnerRcbOptFlds.ENTRYID);
        if (conf_revision.value()) packed |= (1 << InnerRcbOptFlds.CONF_REVISION);
        if (segmentation.value()) packed |= (1 << InnerRcbOptFlds.SEGMENTATION);
        ((InnerRcbOptFlds) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerRcbOptFlds) inner).value;
        sequence_number.value((packed >> InnerRcbOptFlds.SEQUENCE_NUMBER & 1) != 0);
        report_time_stamp.value((packed >> InnerRcbOptFlds.REPORT_TIME_STAMP & 1) != 0);
        reason_for_inclusion.value((packed >> InnerRcbOptFlds.REASON_FOR_INCLUSION & 1) != 0);
        data_set_name.value((packed >> InnerRcbOptFlds.DATA_SET_NAME & 1) != 0);
        data_reference.value((packed >> InnerRcbOptFlds.DATA_REFERENCE & 1) != 0);
        buffer_overflow.value((packed >> InnerRcbOptFlds.BUFFER_OVERFLOW & 1) != 0);
        entry_id.value((packed >> InnerRcbOptFlds.ENTRYID & 1) != 0);
        conf_revision.value((packed >> InnerRcbOptFlds.CONF_REVISION & 1) != 0);
        segmentation.value((packed >> InnerRcbOptFlds.SEGMENTATION & 1) != 0);
    }
}
