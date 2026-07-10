package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * RcbOptFlds ::= BIT STRING (SIZE(10)) — 7.6.4 PER: align + 2 bytes (10 bits)
 *
 * All-pointer container: [0] sequence_number [8] report_time_stamp [16]
 * reason_for_inclusion [24] data_set_name [32] data_reference [40]
 * buffer_overflow [48] entry_id [56] conf_revision [64] segmentation
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
        super(Codec.RCB_OPT_FLDS);
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

    public CmsRcbOptFlds sequence_number(boolean v) {
        this.sequence_number.value(v);
        return this;
    }
    public CmsRcbOptFlds report_time_stamp(boolean v) {
        this.report_time_stamp.value(v);
        return this;
    }
    public CmsRcbOptFlds reason_for_inclusion(boolean v) {
        this.reason_for_inclusion.value(v);
        return this;
    }
    public CmsRcbOptFlds data_set_name(boolean v) {
        this.data_set_name.value(v);
        return this;
    }
    public CmsRcbOptFlds data_reference(boolean v) {
        this.data_reference.value(v);
        return this;
    }
    public CmsRcbOptFlds buffer_overflow(boolean v) {
        this.buffer_overflow.value(v);
        return this;
    }
    public CmsRcbOptFlds entry_id(boolean v) {
        this.entry_id.value(v);
        return this;
    }
    public CmsRcbOptFlds conf_revision(boolean v) {
        this.conf_revision.value(v);
        return this;
    }
    public CmsRcbOptFlds segmentation(boolean v) {
        this.segmentation.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(sequence_number, report_time_stamp, reason_for_inclusion, data_set_name, data_reference, buffer_overflow,
                entry_id, conf_revision, segmentation);
    }
}
