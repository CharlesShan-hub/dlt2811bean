package com.ysh.dlt2811bean.service;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;

/**
 * CMS 服务基类。
 *
 * <p>所有 CMS 服务（Request / Response）继承此类。
 * 基类封装了 APCH 帧头的编解码，子类只需关注 ASDU 服务数据的编解码。
 *
 * <p>APCH 帧结构（5 字节，原始字节）：
 * <pre>
 * ┌──────────┬───────┬──────────┬──────────┐
 * │ PI (1B)  │SC(1B) │ Flags(1B)│ FL (2B)  │
 * │ =0x01    │服务码  │          │ 帧长度   │
 * └──────────┴───────┴──────────┴──────────┘
 * </pre>
 *
 * <p>Flags 字节布局：
 * <pre>
 * bit7: Resp (0=请求, 1=响应)
 * bit6: Err  (0=肯定, 1=否定)
 * bit5: Next (0=末帧, 1=后续帧)
 * bit4~0: 保留
 * </pre>
 *
 * <p>使用示例：
 * <pre>
 *   // 编码
 *   AssociateRequest req = new AssociateRequest();
 *   req.setProtocolVersion(1);
 *   byte[] frame = req.encode();
 *
 *   // 解码
 *   AssociateRequest req = new AssociateRequest();
 *   req.decode(frame);
 * </pre>
 */
public abstract class CmsService {

    /** 协议标识，固定 0x01 */
    private static final byte PI = 0x01;

    /** APCH 帧头长度：5 字节 */
    protected static final int APCH_SIZE = 5;

    /** 服务码 (1 字节) */
    private final int serviceCode;

    /** 请求/响应标志 */
    private boolean response;
    /** 错误标志（仅响应有效） */
    private boolean error;
    /** 分片标志（是否还有后续帧） */
    private boolean fragmented;

    protected CmsService(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    // ==================== 帧头属性 ====================

    public int getServiceCode() { return serviceCode; }
    public boolean isResponse() { return response; }
    public void setResponse(boolean response) { this.response = response; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public boolean isFragmented() { return fragmented; }
    public void setFragmented(boolean fragmented) { this.fragmented = fragmented; }

    // ==================== 编码入口 ====================

    /**
     * 编码为完整帧（APCH + ASDU）。
     *
     * @return 完整的字节帧
     */
    public final byte[] encode() {
        byte[] asdu = encodeAsdu();
        int fl = asdu.length;

        byte[] frame = new byte[APCH_SIZE + fl];
        // APCH
        frame[0] = PI;
        frame[1] = (byte) serviceCode;
        frame[2] = encodeFlags();
        frame[3] = (byte) ((fl >> 8) & 0xFF);
        frame[4] = (byte) (fl & 0xFF);
        // ASDU
        System.arraycopy(asdu, 0, frame, APCH_SIZE, fl);
        return frame;
    }

    /**
     * 解码完整帧（APCH + ASDU）。
     *
     * @param frame 完整的字节帧
     * @throws PerDecodeException 如果帧格式无效或服务数据解码失败
     */
    public final void decode(byte[] frame) throws PerDecodeException {
        if (frame == null || frame.length < APCH_SIZE) {
            throw new PerDecodeException("Frame too short: " + (frame == null ? "null" : frame.length));
        }
        // 校验 PI
        if ((frame[0] & 0xFF) != (PI & 0xFF)) {
            throw new PerDecodeException("Invalid PI: 0x" + String.format("%02X", frame[0]));
        }
        // 校验服务码
        if ((frame[1] & 0xFF) != serviceCode) {
            throw new PerDecodeException(
                "Service code mismatch: expected " + serviceCode + ", got " + (frame[1] & 0xFF));
        }
        // 解析 Flags
        decodeFlags(frame[2]);
        // 解析 FL
        int fl = ((frame[3] & 0xFF) << 8) | (frame[4] & 0xFF);
        // 提取 ASDU
        if (frame.length < APCH_SIZE + fl) {
            throw new PerDecodeException(
                "Frame too short: expected " + (APCH_SIZE + fl) + ", got " + frame.length);
        }
        byte[] asdu = new byte[fl];
        System.arraycopy(frame, APCH_SIZE, asdu, 0, fl);
        // 子类解码
        decodeAsdu(new PerInputStream(asdu));
    }

    // ==================== 子类实现 ====================

    /**
     * 子类实现：将服务参数编码为 ASDU 字节。
     *
     * @return ASDU 字节数组
     */
    protected abstract byte[] encodeAsdu();

    /**
     * 子类实现：从 PER 输入流解码服务参数。
     *
     * @param pis PER 输入流（已跳过 APCH）
     * @throws PerDecodeException 解码失败
     */
    protected abstract void decodeAsdu(PerInputStream pis) throws PerDecodeException;

    // ==================== Flags 编解码 ====================

    private byte encodeFlags() {
        int flags = 0;
        if (response)   flags |= 0x80;  // bit7
        if (error)      flags |= 0x40;  // bit6
        if (fragmented) flags |= 0x20;  // bit5
        return (byte) flags;
    }

    private void decodeFlags(byte flagsByte) {
        this.response   = (flagsByte & 0x80) != 0;
        this.error      = (flagsByte & 0x40) != 0;
        this.fragmented = (flagsByte & 0x20) != 0;
    }

    // ==================== 工具方法 ====================

    /**
     * int 转 2 字节大端数组（用于 ReqID 等固定 2 字节字段）。
     */
    protected static byte[] intToBytes2(int value) {
        return new byte[]{
            (byte) ((value >> 8) & 0xFF),
            (byte) (value & 0xFF)
        };
    }

    /**
     * 2 字节大端数组转 int。
     */
    protected static int bytes2ToInt(byte[] b) {
        return ((b[0] & 0xFF) << 8) | (b[1] & 0xFF);
    }
}
