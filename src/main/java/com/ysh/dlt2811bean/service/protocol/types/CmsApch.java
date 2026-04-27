package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import lombok.Getter;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * APCH (Application Protocol Control Header) — 5-byte frame header.
 *
 * <p>Frame structure:
 * <pre>
 * ┌──────────┬────────┬──────────┬──────────┐
 * │ PI (1B)  │SC(1B)  │ Flags(1B)│ FL (2B)  │
 * │ =0x01    │ SvcCode│          │ FrameLen │
 * └──────────┴────────┴──────────┴──────────┘
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
 * <p>Usage example (recommended):
 * <pre>{@code
 * // Encode — static write ensures all fields are set
 * PerOutputStream pos = new PerOutputStream();
 * CmsApch.write(pos, ServiceCode.ASSOCIATE, MessageType.REQUEST, false, 256);
 * byte[] frame = pos.toByteArray(); // 5 bytes
 *
 * // Decode — static read
 * CmsApch decoded = CmsApch.read(new PerInputStream(frame));
 * ServiceCode sc = decoded.getServiceCode();
 * MessageType mt = decoded.getMessageType();
 * int fl = decoded.getFrameLength();
 * }</pre>
 *
 * <p>Equivalent builder-style usage:
 * <pre>{@code
 * CmsApch apch = new CmsApch()
 *     .withServiceCode(ServiceCode.ASSOCIATE)
 *     .withMessageType(MessageType.REQUEST)
 *     .withFragmented(false)
 *     .withFrameLength(256);
 * PerOutputStream pos = new PerOutputStream();
 * apch.encode(pos);
 * }</pre>
 */
@Getter
public class CmsApch extends AbstractCmsCompound<CmsApch> {

    /** Protocol identifier, fixed 0x01 */
    public CmsInt8U pi = new CmsInt8U(0x01);

    /** Service code */
    public CmsInt8U sc = new CmsInt8U(0);

    /** Flags byte: Resp(bit7) | Err(bit6) | Next(bit5) */
    public CmsInt8U flags = new CmsInt8U(0);

    /** Frame length (excluding APCH) */
    public CmsInt16U fl = new CmsInt16U(0);

    public CmsApch() {
        super("APCH");
        registerField("pi");
        registerField("sc");
        registerField("flags");
        registerField("fl");
    }

    // ==================== High-level Setters ====================

    public CmsApch withServiceCode(ServiceCode serviceCode) {
        this.sc.set(serviceCode.getCode() & 0xFF);
        return this;
    }

    public CmsApch withMessageType(MessageType messageType) {
        int v = flags.get();
        if (messageType.isResponse()) v |= 0x80; else v &= ~0x80;
        if (messageType.isError())    v |= 0x40; else v &= ~0x40;
        flags.set(v);
        return this;
    }

    public CmsApch withFragmented(boolean fragmented) {
        int v = flags.get();
        if (fragmented) v |= 0x20; else v &= ~0x20;
        flags.set(v);
        return this;
    }

    public CmsApch withFrameLength(int frameLength) {
        this.fl.set(frameLength);
        return this;
    }

    // ==================== High-level Getters ====================

    public MessageType getMessageType() {
        return MessageType.fromFlags(
            (flags.get() & 0x80) != 0,
            (flags.get() & 0x40) != 0
        );
    }

    public boolean isFragmented() {
        return (flags.get() & 0x20) != 0;
    }

    public int getFrameLength() {
        return fl.get();
    }

    public ServiceCode getServiceCode() {
        return ServiceCode.fromByte(sc.get().byteValue());
    }

    // ==================== Static Convenience Methods ====================

    /**
     * Read an APCH from a PER input stream.
     *
     * @param pis PER input stream
     * @return decoded APCH
     */
    public static CmsApch read(PerInputStream pis) throws Exception {
        CmsApch apch = new CmsApch();
        apch.decode(pis);
        return apch;
    }

    /**
     * Write an APCH to a PER output stream.
     *
     * @param pos  PER output stream
     * @param apch APCH to encode
     */
    public static void write(PerOutputStream pos, CmsApch apch) {
        apch.encode(pos);
    }

    /**
     * Build and write an APCH in one call — ensures all fields are set.
     *
     * @param pos         PER output stream
     * @param serviceCode service code
     * @param messageType message type (request/response/error)
     * @param fragmented  whether the frame is fragmented
     * @param frameLength frame length (excluding APCH)
     */
    public static void write(PerOutputStream pos, ServiceCode serviceCode, MessageType messageType, boolean fragmented, int frameLength) {
        new CmsApch()
            .withServiceCode(serviceCode)
            .withMessageType(messageType)
            .withFragmented(fragmented)
            .withFrameLength(frameLength)
            .encode(pos);
    }

    @Override
    protected void validate() {
        if (pi.get() != 0x01) {
            throw new IllegalStateException("Invalid PI: expected 0x01, got 0x" + Integer.toHexString(pi.get()));
        }
        if (sc.get() == 0) {
            throw new IllegalStateException("Service code (sc) not set — call withServiceCode() before encode");
        }
    }
}
