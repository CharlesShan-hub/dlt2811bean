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
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.datatypes.type.CmsField;

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
 * or Operate command before it has been completed or executed. This allows a client
 * to withdraw a control request in cases where it was issued in error or the
 * operational conditions have changed.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client cancel request for a pending operation</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming cancellation</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASN.1 (BER/Raw) encoded field layout:
 * <pre>
 * Request ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Response-Positive ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Response-Negative ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   addCause     [7] IMPLICIT AddCause             -- 注意：此处Tag应为[7]，对应图片定义
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsCancel extends CmsAsdu<CmsCancel> {

    // ==================== Fields based on Table 68 ====================

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsData<?> ctlVal = new CmsData<>();

    @CmsField(optional = true, only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsUtcTime operTm = new CmsUtcTime();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsOriginator origin = new CmsOriginator();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsInt8U ctlNum = new CmsInt8U();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {REQUEST, RESPONSE_POSITIVE, RESPONSE_NEGATIVE})
    public CmsBoolean test = new CmsBoolean();

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsCancel() {
        super(ServiceName.CANCEL);
    }

    public CmsCancel(MessageType messageType) {
        super(ServiceName.CANCEL, messageType);
    }

    public CmsCancel(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsCancel reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsCancel ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsCancel ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsCancel test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsCancel addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }
}
