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
 * CMS Service Code 0x47 — Operate (operate service).
 *
 * Corresponds to Table 67 in GB/T 45906.3-2025: Operate service parameters.
 *
 * Service code: 0x47 (71)
 * Service interface: Operate
 * Category: Control service
 *
 * The Operate service is used to execute a control command on a previously
 * selected object. This follows the Select or SelectWithValue service in
 * a two-step control process. It sends the final command to change the
 * state of the device (e.g., opening/closing a breaker).
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client operate request with control value</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming execution</li>
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
 * │ ctlVal                      Data (depends on model)          │
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
 * │ ctlVal                      Data (depends on model)          │
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
 * Operate-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check
 * }
 *
 * Operate-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check
 * }
 *
 * Operate-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check,
 *   addCause     [7] IMPLICIT AddCause
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsOperate extends CmsAsdu<CmsOperate> {

    // ==================== Fields based on Table 67 ====================

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsData<?> ctlVal = new CmsData<>();

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

    public CmsOperate() {
        super(ServiceName.OPERATE);
    }

    public CmsOperate(MessageType messageType) {
        super(ServiceName.OPERATE, messageType);
    }

    public CmsOperate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsOperate reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsOperate ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsOperate ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsOperate test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsOperate addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }
}
