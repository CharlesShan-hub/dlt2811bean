package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsErrorGocbChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x66 — GetGoCBValues (get GOOSE control block values).
 *
 * Corresponds to Table 60 in GB/T 45906.3-2025: GetGoCBValues service parameters.
 *
 * Service code: 0x66 (102)
 * Service interface: GetGoCBValues
 * Category: GOOSE control block service
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetGoCBValues extends CmsAsdu<CmsGetGoCBValues> {

    // ==================== Fields based on Table 60 ====================

    // --- Request parameters ---
    public CmsArray<CmsObjectReference> gocbReference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    // --- Response+ parameters ---
    public CmsArray<CmsErrorGocbChoice> errorGocb = new CmsArray<>(CmsErrorGocbChoice::new).capacity(100);
    public CmsBoolean moreFollows = new CmsBoolean(true);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetGoCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("gocbReference");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("errorGocb");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetGoCBValues does not support " + messageType);
        }
    }

    public CmsGetGoCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetGoCBValues addGocbReference(String ref) {
        this.gocbReference.add(new CmsObjectReference(ref));
        return this;
    }

    public CmsGetGoCBValues addErrorGocbChoice(CmsErrorGocbChoice choice) {
        this.errorGocb.add(choice);
        return this;
    }

    public CmsGetGoCBValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_GOCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetGoCBValues copy() {
        CmsGetGoCBValues copy = new CmsGetGoCBValues(messageType());
        copy.reqId.set(reqId.get());
        copy.gocbReference = this.gocbReference.copy();
        copy.errorGocb = this.errorGocb.copy();
        copy.moreFollows = this.moreFollows.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetGoCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetGoCBValues) new CmsGetGoCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetGoCBValues getGoCBValues) {
        getGoCBValues.encode(pos);
    }

}
