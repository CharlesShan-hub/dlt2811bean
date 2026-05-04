package com.ysh.dlt2811bean.service.svc.control;

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
 * CMS Service Code 0x48 — Cancel (cancel service).
 *
 * Corresponds to Table 68 in GB/T 45906.3-2025: Cancel service parameters.
 *
 * Service code: 0x48 (72)
 * Service interface: Cancel
 * Category: Control service
 *
 * The Cancel service is used to abort a previously issued Select, SelectWithValue,
 * or Operate command before it has been completed or executed.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Cancel a pending control command request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming cancellation</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ operTm                      TimeStamp (OPTIONAL)             │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ operTm                      TimeStamp (OPTIONAL)             │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ operTm                      TimeStamp (OPTIONAL)             │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ addCause                    AddCause                         │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Cancel-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Cancel-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Cancel-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   addCause     [7] IMPLICIT AddCause
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsCommandTermination extends CmsAsdu<CmsCommandTermination> {

    // ==================== Fields based on Table 69 ====================

    // --- Common fields ---
    public CmsObjectReference reference = new CmsObjectReference();
    public CmsData ctlVal = new CmsData<>();
    public CmsUtcTime operTm = new CmsUtcTime();
    public CmsOriginator origin = new CmsOriginator();
    public CmsInt8U ctlNum = new CmsInt8U();
    public CmsUtcTime t = new CmsUtcTime();
    public CmsBoolean test = new CmsBoolean();

    // --- RESPONSE_NEGATIVE only ---
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsCommandTermination(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("addCause");
        } else {
            throw new IllegalArgumentException("CommandTermination does not support " + messageType);
        }
    }

    public CmsCommandTermination(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsCommandTermination reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsCommandTermination ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsCommandTermination ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsCommandTermination test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsCommandTermination addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.COMMAND_TERMINATION;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsCommandTermination copy() {
        CmsCommandTermination copy = new CmsCommandTermination(messageType());
        copy.reqId.set(reqId.get());
        copy.reference = this.reference.copy();
        copy.ctlVal = this.ctlVal.copy();
        copy.operTm = this.operTm.copy();
        copy.origin = this.origin.copy();
        copy.ctlNum = this.ctlNum.copy();
        copy.t = this.t.copy();
        copy.test = this.test.copy();
        copy.addCause = this.addCause.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsCommandTermination read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsCommandTermination) new CmsCommandTermination(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsCommandTermination commandTermination) {
        commandTermination.encode(pos);
    }

}
