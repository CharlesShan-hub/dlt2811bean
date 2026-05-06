package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesResultEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x6A — SetMSVCBValues (set multicast sampling value control block values).
 *
 * Corresponds to Table 64 in GB/T 45906.3-2025: SetMSVCBValues service parameters.
 *
 * Service code: 0x6A (106)
 * Service interface: SetMSVCBValues
 * Category: MSV control block service
 *
 * The SetMSVCBValues service is used to modify the configuration parameters
 * of one or more Multicast Sampling Value Control Blocks (MSVCB). The service allows changing
 * attributes such as the enable state, SV ID, dataset reference, sampling mode, and rate.
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set MSVCB values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * Note: The positive response PDU is NULL (no response body), so only error responses
 * are explicitly defined in the ASN.1 for negative cases.
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ msvcb[0..n]                 SEQUENCE OF SEQUENCE             │
 * │   ├─ reference              ObjectReference                  │
 * │   ├─ svEna                  BOOLEAN (OPTIONAL)               │
 * │   ├─ msvID                  VisibleString129 (OPTIONAL)      │
 * │   ├─ datSet                 ObjectReference (OPTIONAL)       │
 * │   ├─ smpMod                 SmpMod (OPTIONAL)                │
 * │   ├─ smpRate                INT16U (OPTIONAL)                │
 * │   └─ optFlds                MSVOptFlds (OPTIONAL)            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ result[0..n]                SEQUENCE OF SEQUENCE             │
 * │   ├─ error                  ServiceError (OPTIONAL)          │
 * │   ├─ svEna                  ServiceError (OPTIONAL)          │
 * │   ├─ msvID                  ServiceError (OPTIONAL)          │
 * │   ├─ datSet                 ServiceError (OPTIONAL)          │
 * │   ├─ smpMod                 ServiceError (OPTIONAL)          │
 * │   ├─ smpRate                ServiceError (OPTIONAL)          │
 * │   └─ optFlds                ServiceError (OPTIONAL)          │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetMSVCBValues-RequestPDU:: = SEQUENCE {
 *   msvcb    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference [0] IMPLICIT ObjectReference,
 *     svEna     [1] IMPLICIT BOOLEAN OPTIONAL,
 *     msvID     [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet    [3] IMPLICIT ObjectReference OPTIONAL,
 *     smpMod    [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate   [6] IMPLICIT INT16U OPTIONAL,
 *     optFlds   [7] IMPLICIT MSVOptFlds OPTIONAL
 *   }
 * }
 *
 * SetMSVCBValues-ResponsePDU:: = NULL
 *
 * SetMSVCBValues-ErrorPDU:: = SEQUENCE {
 *   result   [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     error     [0] IMPLICIT ServiceError OPTIONAL,
 *     svEna     [1] IMPLICIT ServiceError OPTIONAL,
 *     msvID     [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet    [3] IMPLICIT ServiceError OPTIONAL,
 *     smpMod    [5] IMPLICIT ServiceError OPTIONAL,
 *     smpRate   [6] IMPLICIT ServiceError OPTIONAL,
 *     optFlds   [7] IMPLICIT ServiceError OPTIONAL
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetMSVCBValues extends CmsAsdu<CmsSetMSVCBValues> {

    // ==================== Fields based on Table 64 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsSetMSVCBValuesEntry> msvcb = new CmsArray<>(CmsSetMSVCBValuesEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsArray<CmsSetMSVCBValuesResultEntry> result = new CmsArray<>(CmsSetMSVCBValuesResultEntry::new).capacity(100);

    // ========================= Constructor ============================

    public CmsSetMSVCBValues() {
        super(ServiceName.SET_MSVCBVALUES);
    }

    public CmsSetMSVCBValues(MessageType messageType) {
        super(ServiceName.SET_MSVCBVALUES, messageType);
    }

    public CmsSetMSVCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetMSVCBValues addMsvcb(CmsSetMSVCBValuesEntry entry) {
        this.msvcb.add(entry);
        return this;
    }

    public CmsSetMSVCBValues addResult(CmsSetMSVCBValuesResultEntry entry) {
        this.result.add(entry);
        return this;
    }
}
