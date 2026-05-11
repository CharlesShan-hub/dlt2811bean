package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesResultEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x67 — SetGoCBValues (set GOOSE control block values).
 *
 * Corresponds to Table 61 in GB/T 45906.3-2025: SetGoCBValues service parameters.
 *
 * Service code: 0x67 (103)
 * Service interface: SetGoCBValues
 * Category: GOOSE control block service
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetGoCBValues extends CmsAsdu<CmsSetGoCBValues> {

    // ==================== Fields based on Table 61 ====================

    @CmsField(only = {REQUEST})
    public CmsArray<CmsSetGoCBValuesEntry> gocb = new CmsArray<>(CmsSetGoCBValuesEntry::new).capacity(100);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsArray<CmsSetGoCBValuesResultEntry> result = new CmsArray<>(CmsSetGoCBValuesResultEntry::new).capacity(100);

    // ========================= Constructor ============================

    public CmsSetGoCBValues() {
        super(ServiceName.SET_GOCB_VALUES);
    }

    public CmsSetGoCBValues(MessageType messageType) {
        super(ServiceName.SET_GOCB_VALUES, messageType);
    }

    public CmsSetGoCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetGoCBValues addGocb(CmsSetGoCBValuesEntry entry) {
        this.gocb.add(entry);
        return this;
    }

    public CmsSetGoCBValues addResult(CmsSetGoCBValuesResultEntry entry) {
        this.result.add(entry);
        return this;
    }
}
