package com.ysh.jcms.utils.transport.frame;

import com.ysh.jcms.core.info.CmsServiceInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * FrameHeader — 4-byte APCH control header, per DL/T 2811 6.1.2.
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
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class FrameHeader {

    public static final int HEADER_SIZE = 4; // APCH = control code + SC + FL
    public static final int PI_DEFAULT = 0x01; // DL/T 2811 6.1.2 a)

    private boolean next;
    private boolean resp;
    private boolean err;
    private int pi = PI_DEFAULT;
    private CmsServiceInfo serviceCode;
    private int frameLength;

    /** Encode header to 4 bytes: CC, SC, FL(lo), FL(hi). */
    public byte[] encode() {
        byte cc = (byte) pi;
        if (next)
            cc |= 0x80;
        if (resp)
            cc |= 0x40;
        if (err)
            cc |= 0x20;

        int sc = serviceCode != null ? serviceCode.serviceCode() : 0; // null SC would silently become 0x00
        if (serviceCode == null) {
            throw new IllegalStateException("FrameHeader.serviceCode must be set before encoding");
        }
        return new byte[]{cc, (byte) sc, (byte) (frameLength & 0xFF), (byte) ((frameLength >> 8) & 0xFF)}; // FL little-endian
    }

    /** Decode header from bytes at offset. */
    public static FrameHeader decode(byte[] data, int offset) {
        if (data.length - offset < HEADER_SIZE) {
            throw new IllegalArgumentException("Header too short");
        }
        byte cc = data[offset];
        return new FrameHeader().next((cc & 0x80) != 0).resp((cc & 0x40) != 0).err((cc & 0x20) != 0).pi(cc & 0x0F)
                .serviceCode(CmsServiceInfo.byCode(data[offset + 1] & 0xFF))
                .frameLength((data[offset + 2] & 0xFF) | ((data[offset + 3] & 0xFF) << 8)); // little-endian
    }
}
