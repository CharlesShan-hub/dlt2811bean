package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.code.CmsCheck;
import com.ysh.dlt2811bean.datatypes.compound.CmsOriginator;
import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x4B — TimeActivatedOperateTermination (time activated operate termination service).
 *
 * Corresponds to Table 71 in GB/T 45906.3-2025: TimeActivatedOperateTermination service parameters.
 *
 * Service code: 0x4A (74)
 * Service interface: TimeActivatedOperateTermination
 * Category: Control service
 *
 * The TimeActivatedOperateTermination service is used to explicitly terminate or cancel
 * a previously scheduled time-activated control operation before its execution time
 * has been reached. This allows a client to withdraw a scheduled command that is
 * no longer required or valid.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to terminate a scheduled operation</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming termination</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * │ addCause                    AddCause OPTIONAL               │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * │ addCause                    AddCause                        │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * TimeActivatedOperateTermination-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check,
 *   addCause     [8] IMPLICIT AddCause OPTIONAL
 * }
 *
 * TimeActivatedOperateTermination-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperateTermination-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check,
 *   addCause     [8] IMPLICIT AddCause
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsTimeActivatedOperateTermination extends CmsAsdu<CmsTimeActivatedOperateTermination> {

    // ==================== Fields based on Table 71 ====================

    // --- Common fields ---
    public CmsObjectReference reference = new CmsObjectReference();
    public CmsData ctlVal = new CmsData<>();
    public CmsUtcTime operTm = new CmsUtcTime();
    public CmsOriginator origin = new CmsOriginator();
    public CmsInt8U ctlNum = new CmsInt8U();
    public CmsUtcTime t = new CmsUtcTime();
    public CmsBoolean test = new CmsBoolean();
    public CmsCheck check = new CmsCheck();
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsTimeActivatedOperateTermination(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("reference");
            registerField("ctlVal");
            registerField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
            registerOptionalField("addCause");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
            registerField("addCause");
        } else {
            throw new IllegalArgumentException("TimeActivatedOperateTermination does not support " + messageType);
        }
    }

    public CmsTimeActivatedOperateTermination(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsTimeActivatedOperateTermination reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsTimeActivatedOperateTermination ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsTimeActivatedOperateTermination ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsTimeActivatedOperateTermination test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsTimeActivatedOperateTermination addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsTimeActivatedOperateTermination copy() {
        CmsTimeActivatedOperateTermination copy = new CmsTimeActivatedOperateTermination(messageType());
        copy.reqId.set(reqId.get());
        copy.reference = this.reference.copy();
        copy.ctlVal = this.ctlVal.copy();
        copy.operTm = this.operTm.copy();
        copy.origin = this.origin.copy();
        copy.ctlNum = this.ctlNum.copy();
        copy.t = this.t.copy();
        copy.test = this.test.copy();
        copy.check = this.check.copy();
        copy.addCause = this.addCause.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsTimeActivatedOperateTermination read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsTimeActivatedOperateTermination) new CmsTimeActivatedOperateTermination(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsTimeActivatedOperateTermination timeActivatedOperateTermination) {
        timeActivatedOperateTermination.encode(pos);
    }

}
