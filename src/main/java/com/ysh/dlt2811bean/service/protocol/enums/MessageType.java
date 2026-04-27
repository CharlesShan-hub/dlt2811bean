package com.ysh.dlt2811bean.service.protocol.enums;

/**
 * Message type for CMS APCH flags (Resp + Err bits).
 *
 * <p>Per section 8.1.2 of the protocol:
 * <ul>
 *   <li><b>Resp</b> (bit7): 0 = Request/Indication/Request+, 1 = Response</li>
 *   <li><b>Err</b> (bit6): 0 = Positive, 1 = Negative (only meaningful for Response)</li>
 * </ul>
 *
 * <p>Note: {@link #INDICATION} and {@link #REQUEST_PLUS} share the same
 * APCH flag encoding as {@link #REQUEST} (Resp=0, Err=0). The distinction
 * is made by the {@link com.ysh.dlt2811bean.service.protocol.enums.ServiceCode}
 * and the service class hierarchy.
 *
 * <p>Use {@link #RESPONSE} when decoding a response frame without knowing
 * whether it is positive or negative. After decoding, the actual type
 * ({@link #RESPONSE_POSITIVE} or {@link #RESPONSE_NEGATIVE}) will be
 * resolved from the APCH Err flag.
 */
public enum MessageType {

    /** Request-ASDU (Resp=0, Err=0) */
    REQUEST(false, false),
    /** Indication-ASDU (Resp=0, Err=0) — server-initiated, no response expected */
    INDICATION(false, false),
    /** Request+-ASDU (Resp=0, Err=0) — server-initiated request */
    REQUEST_PLUS(false, false),
    /** Unknown response type — resolved to POSITIVE or NEGATIVE during decode */
    RESPONSE(true, false),
    /** Positive Response-ASDU (Resp=1, Err=0) */
    RESPONSE_POSITIVE(true, false),
    /** Negative Response-ASDU (Resp=1, Err=1) */
    RESPONSE_NEGATIVE(true, true);

    private final boolean response;
    private final boolean error;

    MessageType(boolean response, boolean error) {
        this.response = response;
        this.error = error;
    }

    public boolean isResponse() {
        return response;
    }

    public boolean isError() {
        return error;
    }

    public static MessageType fromFlags(boolean response, boolean error) {
        if (!response) return REQUEST;
        return error ? RESPONSE_NEGATIVE : RESPONSE_POSITIVE;
    }
}