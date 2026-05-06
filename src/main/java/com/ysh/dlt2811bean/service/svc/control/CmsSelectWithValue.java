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

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsObjectReference reference = new CmsObjectReference();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsData ctlVal = new CmsData<>();

    @CmsField(optional = true, only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsUtcTime operTm = new CmsUtcTime();
    
    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsOriginator origin = new CmsOriginator();
    
    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsInt8U ctlNum = new CmsInt8U();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsBoolean test = new CmsBoolean();

    @CmsField(only = {"REQUEST", "RESPONSE_POSITIVE", "RESPONSE_NEGATIVE"})
    public CmsCheck check = new CmsCheck();

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsAddCause addCause = new CmsAddCause();

    // ========================= Constructor ============================

    public CmsSelectWithValue() {
        super(ServiceName.SELECT_WITH_VALUE);
    }

    public CmsSelectWithValue(MessageType messageType) {
        super(ServiceName.SELECT_WITH_VALUE, messageType);
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
}
