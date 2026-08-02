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

    public int getWireSize() {
        return 2 + FrameHeader.HEADER_SIZE + 2 + asduBytes.length; // FL + APCH + ReqID + Data
    }
}
