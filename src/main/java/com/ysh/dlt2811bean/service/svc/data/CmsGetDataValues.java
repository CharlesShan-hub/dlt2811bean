package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x56 — GetDataValues (read data values).
 *
 * Corresponds to Table 31 in GB/T 45906.3-2025: GetDataValues service parameters.
 *
 * Service code: 0x30 (48)
 * Service interface: GetDataValues
 * Category: Data access service
 *
 * The GetDataValues service is used to retrieve the values of a set of data objects
 * or data attributes. The fc parameter is used to specify functional constraints
 * for filtering specific categories of data attributes. If fc is XX or empty, no filtering is applied.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data values</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   reference                ObjectReference                  │
 * │   fc                       FunctionalConstraint OPTIONAL    │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ value[0..n]                SEQUENCE OF Data                 │
 * │ moreFollows                BOOLEAN DEFAULT TRUE             │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetDataValues-RequestPDU::= SEQUENCE {
 *   data    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference [0] IMPLICIT ObjectReference,
 *     fc        [1] IMPLICIT FunctionalConstraint OPTIONAL
 *   }
 * }
 *
 * GetDataValues-ResponsePDU::= SEQUENCE {
 *   value        [0] IMPLICIT SEQUENCE OF Data,
 *   moreFollows  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetDataValues extends CmsAsdu<CmsGetDataValues> {

    // ==================== Fields based on Table 31 ====================

    // --- Request parameters ---
    public CmsArray<CmsGetDataValuesEntry> data = new CmsArray<>(CmsGetDataValuesEntry::new).capacity(100);

    // --- Response+ parameters ---
    public CmsStructure value = new CmsStructure().capacity(100);
    public CmsBoolean moreFollows = new CmsBoolean(true);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetDataValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("data");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("value");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetDataValues does not support " + messageType);
        }
    }

    public CmsGetDataValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetDataValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    public CmsGetDataValues moreFollows(boolean moreFollows) {
        this.moreFollows.set(moreFollows);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetDataValues copy() {
        CmsGetDataValues copy = new CmsGetDataValues(messageType());
        copy.reqId.set(reqId.get());
        copy.data = this.data.copy();
        copy.value = this.value.copy();
        copy.moreFollows = this.moreFollows.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetDataValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetDataValues) new CmsGetDataValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetDataValues getDataValues) {
        getDataValues.encode(pos);
    }

}
