package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsReasonCode() {
        this.data_change           = new CmsBoolean();
        this.quality_change        = new CmsBoolean();
        this.data_update           = new CmsBoolean();
        this.integrity             = new CmsBoolean();
        this.general_interrogation = new CmsBoolean();
        this.application_trigger   = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(data_change, quality_change, data_update,
                             integrity, general_interrogation, application_trigger);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeReasonCode(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeReasonCode(nativePtr, data); read(); }
}
