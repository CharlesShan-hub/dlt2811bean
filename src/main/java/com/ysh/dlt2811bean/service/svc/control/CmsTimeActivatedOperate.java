package com.ysh.dlt2811bean.service.svc.control;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
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

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsTimeActivatedOperate(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("TimeActivatedOperate does not support " + messageType);
        }
    }

    public CmsTimeActivatedOperate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TIME_ACTIVATED_OPERATE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsTimeActivatedOperate copy() {
        CmsTimeActivatedOperate copy = new CmsTimeActivatedOperate(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsTimeActivatedOperate read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsTimeActivatedOperate) new CmsTimeActivatedOperate(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsTimeActivatedOperate timeActivatedOperate) {
        timeActivatedOperate.encode(pos);
    }

}
