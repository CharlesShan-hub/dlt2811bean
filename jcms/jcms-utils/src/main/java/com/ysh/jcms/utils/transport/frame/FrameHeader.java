package com.ysh.jcms.utils.transport.frame;

import com.ysh.jcms.utils.transport.ServiceName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * FrameHeader — 4-byte protocol control header.
 *
 * <pre>
 * CC byte:
 * bit7: Next  (0=last, 1=more fragments)
 * bit6: Resp  (0=request, 1=response)
 * bit5: Err   (0=positive, 1=negative)
 * bit4: bak   (reserved, 0)
 * bit3~0: PI  (protocol identifier, 0x01)
 * </pre>
 */
@Getter @Setter
@Accessors(fluent = true, chain = true)
public class FrameHeader {

    public static final int HEADER_SIZE = 4;
    private static final int PI_DEFAULT = 0x01;

    private boolean next;
    private boolean resp;
    private boolean err;
    private ServiceName serviceCode;
    private int frameLength;

    /** Encode header to 4 bytes: CC, SC, FL(hi), FL(lo). */
    public byte[] encode() {
        byte cc = PI_DEFAULT;
        if (next) cc |= 0x80;
        if (resp) cc |= 0x40;
        if (err)  cc |= 0x20;

        int sc = serviceCode != null ? serviceCode.getCode() : 0;
        return new byte[] {
            cc,
            (byte) sc,
            (byte) ((frameLength >> 8) & 0xFF),
            (byte) (frameLength & 0xFF)
        };
    }

    /** Decode header from bytes at offset. */
    public static FrameHeader decode(byte[] data, int offset) {
        if (data.length - offset < HEADER_SIZE) {
            throw new IllegalArgumentException("Header too short");
        }
        byte cc = data[offset];
        return new FrameHeader()
            .next((cc & 0x80) != 0)
            .resp((cc & 0x40) != 0)
            .err( (cc & 0x20) != 0)
            .serviceCode(ServiceName.fromCode(data[offset + 1] & 0xFF))
            .frameLength(((data[offset + 2] & 0xFF) << 8) | (data[offset + 3] & 0xFF));
    }
}
