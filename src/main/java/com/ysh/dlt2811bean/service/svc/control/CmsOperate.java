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

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsOperate(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("Operate does not support " + messageType);
        }
    }

    public CmsOperate(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.OPERATE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsOperate copy() {
        CmsOperate copy = new CmsOperate(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsOperate read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsOperate) new CmsOperate(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsOperate operate) {
        operate.encode(pos);
    }

}
