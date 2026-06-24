package com.ysh.jcms.utils.transport.frame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Frame — a complete protocol frame = {@link FrameHeader} + ASDU bytes.
 */
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Frame {

    public static final int MAX_PAYLOAD_SIZE = 65531;

    private final FrameHeader header;
    private final byte[] asduBytes;
    private final int reqId;

    public Frame(FrameHeader header, byte[] asduBytes) {
        this(header, asduBytes, extractReqId(asduBytes));
    }

    public int getWireSize() {
        return 2 + FrameHeader.HEADER_SIZE + asduBytes.length;
    }

    private static int extractReqId(byte[] asdu) {
        if (asdu == null || asdu.length < 2) return 0;
        return ((asdu[0] & 0xFF) << 8) | (asdu[1] & 0xFF);
    }
}
