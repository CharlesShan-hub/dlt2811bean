package com.ysh.dlt2811bean.service.types;

import com.ysh.dlt2811bean.service.enums.MessageType;
import com.ysh.dlt2811bean.service.enums.ServiceCode;
import lombok.Getter;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;

/**
 * Base class for all CMS services (Request / Response).
 *
 * <p>Encapsulates APCH header encoding/decoding.
 * Subclasses only need to implement ASDU data encoding/decoding.
 *
 * <p>APCH frame structure (5 bytes, raw):
 * <pre>
 * ┌──────────┬───────┬──────────┬──────────┐
 * │ PI (1B)  │SC(1B) │ Flags(1B)│ FL (2B)  │
 * │ =0x01    │ SvcCode│          │ FrameLen │
 * └──────────┴───────┴──────────┴──────────┘
 * </pre>
 *
 * <p>Flags byte layout:
 * <pre>
 * bit7: Resp (0=request, 1=response)
 * bit6: Err  (0=positive, 1=negative)
 * bit5: Next (0=last, 1=more fragments)
 * bit4~0: reserved
 * </pre>
 *
 * <p>Usage example:
 * <pre>
 *   // Encode
 *   AssociateRequest req = new AssociateRequest();
 *   req.setProtocolVersion(1);
 *   byte[] frame = req.encode();
 *
 *   // Decode
 *   AssociateRequest req = new AssociateRequest();
 *   req.decode(frame);
 * </pre>
 */
public abstract class CmsService {

    /** Protocol identifier, fixed 0x01 */
    private static final byte PI = 0x01;

    /** APCH header length: 5 bytes */
    protected static final int APCH_SIZE = 5;

    /** Service code */
    @Getter
    private final ServiceCode serviceCode;

    /** Message type (request / positive response / negative response) */
    @Getter
    private MessageType messageType = MessageType.REQUEST;
    /** Fragment flag (more frames follow if true) */
    @Getter
    private boolean fragmented;

    protected CmsService(ServiceCode serviceCode) {
        this.serviceCode = serviceCode;
    }

    // ==================== Header Setters ====================

    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    public void setFragmented(boolean fragmented) { this.fragmented = fragmented; }

    // ==================== Encode/Decode ====================

    /**
     * Encode into a complete frame (APCH + ASDU).
     *
     * @return the complete byte frame
     */
    public final byte[] encode() {
        byte[] asdu = encodeAsdu();
        int fl = asdu.length;

        byte[] frame = new byte[APCH_SIZE + fl];
        // APCH
        frame[0] = PI;
        frame[1] = serviceCode.getCode();
        frame[2] = encodeFlags();
        frame[3] = (byte) ((fl >> 8) & 0xFF);
        frame[4] = (byte) (fl & 0xFF);
        // ASDU
        System.arraycopy(asdu, 0, frame, APCH_SIZE, fl);
        return frame;
    }

    /**
     * Decode a complete frame (APCH + ASDU).
     *
     * @param frame the complete byte frame
     * @throws PerDecodeException if the frame format is invalid or ASDU decoding fails
     */
    public final void decode(byte[] frame) throws PerDecodeException {
        if (frame == null || frame.length < APCH_SIZE) {
            throw new PerDecodeException("Frame too short: " + (frame == null ? "null" : frame.length));
        }
        // verify PI
        if ((frame[0] & 0xFF) != (PI & 0xFF)) {
            throw new PerDecodeException("Invalid PI: 0x" + String.format("%02X", frame[0]));
        }
        // verify service code
        if ((frame[1] & 0xFF) != serviceCode.getCode()) {
            throw new PerDecodeException(
                "Service code mismatch: expected " + serviceCode.getCode() + ", got " + (frame[1] & 0xFF));
        }
        // parse Flags
        decodeFlags(frame[2]);
        // parse FL
        int fl = ((frame[3] & 0xFF) << 8) | (frame[4] & 0xFF);
        // extract ASDU
        if (frame.length < APCH_SIZE + fl) {
            throw new PerDecodeException(
                "Frame too short: expected " + (APCH_SIZE + fl) + ", got " + frame.length);
        }
        byte[] asdu = new byte[fl];
        System.arraycopy(frame, APCH_SIZE, asdu, 0, fl);
        // subclass decode
        decodeAsdu(new PerInputStream(asdu));
    }

    // ==================== Subclass Hooks ====================

    /**
     * Subclass implements: encode service parameters into ASDU bytes.
     *
     * @return ASDU byte array
     */
    protected abstract byte[] encodeAsdu();

    /**
     * Subclass implements: decode service parameters from PER input stream.
     *
     * @param pis PER input stream (APCH already consumed)
     * @throws PerDecodeException on decoding failure
     */
    protected abstract void decodeAsdu(PerInputStream pis) throws PerDecodeException;

    // ==================== Flags ====================

    private byte encodeFlags() {
        int flags = 0;
        if (messageType.isResponse())   flags |= 0x80;  // bit7
        if (messageType.isError())      flags |= 0x40;  // bit6
        if (fragmented)                 flags |= 0x20;  // bit5
        return (byte) flags;
    }

    private void decodeFlags(byte flagsByte) {
        this.messageType = MessageType.fromFlags(
            (flagsByte & 0x80) != 0,
            (flagsByte & 0x40) != 0
        );
        this.fragmented = (flagsByte & 0x20) != 0;
    }

}
