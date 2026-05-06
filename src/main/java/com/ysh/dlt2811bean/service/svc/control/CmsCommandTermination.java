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
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x49 — Command Termination (cancel service).
 *
 * Corresponds to Table 69 in GB/T 45906.3-2025: CommandTermination service parameters.
 *
 * Service code: 0x49 (73)
 * Service interface: CommandTermination
 * Category: Control service
 *
 * The CommandTermination service is used to abort a previously issued Select, SelectWithValue,
 * or Operate command before it has been completed or executed.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST_POSITIVE - Server positive response confirming termination</li>
 *   <li>REQUEST_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * REQUEST+ ASDU:
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
 * REQUEST- ASDU:
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
 * CommandTermination-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   addCause     [7] IMPLICIT AddCause OPTIONAL
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsCommandTermination extends CmsAsdu<CmsCommandTermination> {

    // ==================== Fields based on Table 69 ====================

    // --- Common fields ---
    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsData<?> ctlVal = new CmsData<>();

    @CmsField(optional = true, only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsUtcTime operTm = new CmsUtcTime();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})  
    public CmsOriginator origin = new CmsOriginator();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsInt8U ctlNum = new CmsInt8U();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsBoolean test = new CmsBoolean();

    @CmsField(optional = true, only = {"REQUEST_NEGATIVE"})
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsCommandTermination() {
        super(ServiceName.COMMAND_TERMINATION);
    }

    public CmsCommandTermination(MessageType messageType) {
        super(ServiceName.COMMAND_TERMINATION, messageType);
    }

    public CmsCommandTermination(boolean isResp, boolean isErr) {
        this(getReqMessageType(isResp, isErr));
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
}
