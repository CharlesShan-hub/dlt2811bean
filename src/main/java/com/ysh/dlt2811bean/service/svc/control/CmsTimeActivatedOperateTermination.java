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
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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
 * Request+ ASDU:
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
 * Request- ASDU:
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
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsTimeActivatedOperateTermination extends CmsAsdu<CmsTimeActivatedOperateTermination> {

    // ==================== Fields based on Table 71 ====================

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsData<?> ctlVal = new CmsData<>();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsUtcTime operTm = new CmsUtcTime();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsOriginator origin = new CmsOriginator();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsInt8U ctlNum = new CmsInt8U();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsBoolean test = new CmsBoolean();

    @CmsField(only = {"REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsCheck check = new CmsCheck();

    @CmsField(optional = true, only = {"REQUEST_NEGATIVE"})
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsTimeActivatedOperateTermination() {
        super(ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION);
    }

    public CmsTimeActivatedOperateTermination(MessageType messageType) {
        super(ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION, messageType);
    }

    public CmsTimeActivatedOperateTermination(boolean isResp, boolean isErr) {
        this(getReqMessageType(isResp, isErr));
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
}
