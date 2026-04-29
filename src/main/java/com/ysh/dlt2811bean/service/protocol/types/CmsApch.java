package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import lombok.Getter;

/**
 * APCH (Application Protocol Control Header) — 4-byte frame header.
 *
 * <p>Frame structure:
 * <pre>
 * ┌──────────┬────────┬──────────┐
 * │ CC (1B)  │SC(1B)  │ FL (2B)  │
 * │ Control  │ SvcCode│ FrameLen │
 * │ Code     │        │          │
 * └──────────┴────────┴──────────┘
 * </pre>
 *
 * <p>Control Code byte layout:
 * <pre>
 * bit7: Next (0=last, 1=more fragments)
 * bit6: Resp (0=request, 1=response)
 * bit5: Err  (0=positive, 1=negative)
 * bit4: bak (reserved)
 * bit3~0: PI (protocol identifier, default 0x01)
 * </pre>
 *
 * <p>Usage:
 * <pre>{@code
 * // Encode
 * CmsApch apch = new CmsApch()
 *     .next(false)
 *     .resp(false)
 *     .err(false)
 *     .serviceCode(ServiceCode.ASSOCIATE)
 *     .frameLength(100);
 * PerOutputStream pos = new PerOutputStream();
 * apch.encode(pos);
 *
 * // Decode
 * CmsApch decoded = CmsApch.read(new PerInputStream(encoded));
 * ServiceCode sc = decoded.getServiceCode();
 * int fl = decoded.getFrameLength();
 * }</pre>
 */
@Getter
public class CmsApch extends AbstractCmsCompound<CmsApch> {

    private boolean[] flags = new boolean[5];

    /** Control code: Next | Resp | Err | bak | PI(4bit) */
    public CmsControlCode cc = new CmsControlCode();

    /** Service code */
    public CmsInt8U sc = new CmsInt8U(0);

    /** Frame length (excluding APCH) */
    public CmsInt16U fl = new CmsInt16U(0);

    public CmsApch() {
        super("APCH");
        registerField("cc");
        registerField("sc");
        registerField("fl");
    }

    // ==================== High-level Setters ====================

    public CmsApch next(boolean next) {
        flags[0] = true;
        this.cc.setNext(next);
        return this;
    }

    public CmsApch resp(boolean resp) {
        flags[1] = true;
        this.cc.setResp(resp);
        return this;
    }

    public CmsApch err(boolean err) {
        flags[2] = true;
        this.cc.setErr(err);
        return this;
    }

    public CmsApch serviceCode(ServiceName serviceName) {
        flags[3] = true;
        this.sc.set(serviceName.getCode());
        return this;
    }

    public CmsApch frameLength(int frameLength) {
        flags[4] = true;
        this.fl.set(frameLength);
        return this;
    }

    public void fromMessageType(MessageType messageType) {
        this.err(messageType.isError()).resp(messageType.isResponse());
    }

    // ==================== High-level Getters ====================

    public int getFrameLength() {
        return fl.get();
    }

    public ServiceName getServiceCode() {
        return ServiceName.fromByte(sc.get().byteValue());
    }

    public boolean isNext() {
        return cc.isNext();
    }

    public boolean isResp() {
        return cc.isResp();
    }

    public boolean isErr() {
        return cc.isErr();
    }

    // ==================== Static Convenience Methods ====================

    public static CmsApch read(PerInputStream pis) throws Exception {
        CmsApch apch = new CmsApch();
        apch.decode(pis);
        return apch;
    }

    public static void write(PerOutputStream pos, CmsApch apch) {
        apch.encode(pos);
    }

    @Override
    public void encode(PerOutputStream pos) {
        if (!flags[0] || !flags[1] || !flags[2] || !flags[3] || !flags[4]) {
            throw new IllegalStateException("Not all fields set — call with*() before encode");
        }
        super.encode(pos);
    }

    @Override
    public CmsApch decode(PerInputStream pis) throws Exception {
        super.decode(pis);
        flags[0] = true;
        flags[1] = true;
        flags[2] = true;
        flags[3] = true;
        flags[4] = true;
        return this;
    }
}
