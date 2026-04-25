package com.ysh.dlt2811bean.service.enums;

/**
 * Message type for CMS APCH flags (Resp + Err bits).
 *
 * <p>Per section 8.1.2 of the protocol:
 * <ul>
 *   <li><b>Resp</b> (bit7): 0 = Request, 1 = Response</li>
 *   <li><b>Err</b> (bit6): 0 = Positive, 1 = Negative (only meaningful for Response)</li>
 * </ul>
 */
public enum MessageType {

    /** Request-ASDU (Resp=0, Err=0) */
    REQUEST(false, false),
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