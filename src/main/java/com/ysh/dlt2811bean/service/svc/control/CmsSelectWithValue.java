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
 * CMS Service Code 0x45 — SelectWithValue (select with value service).
 *
 * Corresponds to Table 66 in GB/T 45906.3-2025: SelectWithValue service parameters.
 *
 * Service code: 0x45 (69)
 * Service interface: SelectWithValue
 * Category: Control service
 *
 * The SelectWithValue service is used to select a controllable object and
 * simultaneously specify the intended control value. This combines the
 * selection step with the operand value definition, often used in contexts
 * where the value to be applied must be confirmed during selection.
 * This two-step process (SelectWithValue followed by Operate) ensures that
 * the intended object and value are ready to be controlled and prevents
 * unauthorized or accidental operations.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client select with value request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming selection and value</li>
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
 * │ check                       Check                            │
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
 * │ check                       Check                            │
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
 * │ check                       Check                            │
 * │ addCause                    AddCause                         │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SelectWithValue-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check
 * }
 *
 * SelectWithValue-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check
 * }
 *
 * SelectWithValue-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
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
public class CmsSelectWithValue extends CmsAsdu<CmsSelectWithValue> {

    // ==================== Fields based on Table 66 ====================

    // --- Common fields ---
    public CmsObjectReference reference = new CmsObjectReference();
    public CmsData ctlVal = new CmsData<>();
    public CmsUtcTime operTm = new CmsUtcTime();
    public CmsOriginator origin = new CmsOriginator();
    public CmsInt8U ctlNum = new CmsInt8U();
    public CmsUtcTime t = new CmsUtcTime();
    public CmsBoolean test = new CmsBoolean();
    public CmsCheck check = new CmsCheck();

    // --- RESPONSE_NEGATIVE only ---
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsSelectWithValue(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("reference");
            registerField("ctlVal");
            registerOptionalField("operTm");
            registerField("origin");
            registerField("ctlNum");
            registerField("t");
            registerField("test");
            registerField("check");
            registerField("addCause");
        } else {
            throw new IllegalArgumentException("SelectWithValue does not support " + messageType);
        }
    }

    public CmsSelectWithValue(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSelectWithValue reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsSelectWithValue ctlVal(CmsType<?> val) {
        this.ctlVal = new CmsData(val);
        return this;
    }

    public CmsSelectWithValue ctlNum(int num) {
        this.ctlNum.set(num);
        return this;
    }

    public CmsSelectWithValue test(boolean test) {
        this.test.set(test);
        return this;
    }

    public CmsSelectWithValue addCause(int cause) {
        this.addCause.set(cause);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_WITH_VALUE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSelectWithValue copy() {
        CmsSelectWithValue copy = new CmsSelectWithValue(messageType());
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
    public static CmsSelectWithValue read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSelectWithValue) new CmsSelectWithValue(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSelectWithValue selectWithValue) {
        selectWithValue.encode(pos);
    }

}
