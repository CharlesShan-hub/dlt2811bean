package com.ysh.dlt2811bean.service.protocol.enums;

import lombok.Getter;

/**
 * Message type for CMS APCH flags (Resp + Err bits).
 *
 * <p>Per section 8.1.2 of the protocol:
 * <ul>
 *   <li><b>Resp</b> (bit7): 0 = Request/Indication/Request+, 1 = Response</li>
 *   <li><b>Err</b> (bit6): 0 = Positive, 1 = Negative (only meaningful for Response)</li>
 * </ul>
 *
 */
@Getter
public enum MessageType {

    /** Request-ASDU (Resp=0, Err=0) */
    REQUEST(false, false),
    /** Request+-ASDU (Resp=0, Err=0) — server-initiated positive request */
    REQUEST_PLUS(false, false),
    /** Request--ASDU (Resp=0, Err=1) — server-initiated negative request */
    REQUEST_NEGATIVE(false, true),
    /** Positive Response-ASDU (Resp=1, Err=0) */
    RESPONSE_POSITIVE(true, false),
    /** Negative Response-ASDU (Resp=1, Err=1) */
    RESPONSE_NEGATIVE(true, true),
    /** Indication-ASDU (Resp=0, Err=0) — server-initiated indication */
    INDICATION(false, false),
    /** Unknown message type */
    UNKNOWN(false, false); // Just to be a placeholder

    private final boolean response;
    private final boolean error;

    MessageType(boolean response, boolean error) {
        this.response = response;
        this.error = error;
    }
}