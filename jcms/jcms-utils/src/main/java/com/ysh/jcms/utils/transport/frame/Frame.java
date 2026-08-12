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

    /**
     * Max ASDU payload bytes in a single frame: FL max (65531) - ReqID(2), per DL/T
     * 2811 6.1.2.
     */
    public static final int MAX_PAYLOAD_SIZE = 65535 - FrameHeader.HEADER_SIZE - 2;

    private final FrameHeader header;
    private final byte[] asduBytes;
    private final int reqId;
}
