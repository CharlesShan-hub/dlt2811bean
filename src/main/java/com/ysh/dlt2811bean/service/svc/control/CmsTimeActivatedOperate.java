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
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x4A — TimeActivatedOperate (time activated operate service).
 *
 * Corresponds to Table 70 in GB/T 45906.3-2025: TimeActivatedOperate service parameters.
 *
 * Service code: 0x4A (74)
 * Service interface: TimeActivatedOperate
 * Category: Control service
 *
 * The TimeActivatedOperate service is used to request a controllable object
 * to perform a control operation at a specific time in the future. This
 * service combines the selection and value definition with a scheduled
 * execution time, allowing for time-synchronized control actions.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to operate at a specific time</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming the timed operation</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * │ addCause                    AddCause                         │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * TimeActivatedOperate-RequestPDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperate-ResponsePDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperate-ErrorPDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check,
 *   addCause         [8] IMPLICIT AddCause
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsTimeActivatedOperate extends CmsAsdu<CmsTimeActivatedOperate> {

    // ==================== Fields based on Table 70 ====================

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsData<?> ctlVal = new CmsData<>();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsUtcTime operTm = new CmsUtcTime();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsOriginator origin = new CmsOriginator();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsInt8U ctlNum = new CmsInt8U();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsBoolean test = new CmsBoolean();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsCheck check = new CmsCheck();

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsTimeActivatedOperate() {
        super(ServiceName.TIME_ACTIVATED_OPERATE);
    }

    public CmsTimeActivatedOperate(MessageType messageType) {
        super(ServiceName.TIME_ACTIVATED_OPERATE, messageType);
    }

    public CmsTimeActivatedOperate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsTimeActivatedOperate reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsTimeActivatedOperate ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsTimeActivatedOperate ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsTimeActivatedOperate test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsTimeActivatedOperate addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }
}
