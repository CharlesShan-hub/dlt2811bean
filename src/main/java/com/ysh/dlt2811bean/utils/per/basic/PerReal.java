package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 REAL 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>REAL</b> (GB/T 16262 / X.680 §24)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>FLOAT32 — 32 位浮点数据值（7.1.4，浮点模拟量）</li>
 *   <li>FLOAT64 — 64 位浮点数据值（7.1.4，高精度浮点模拟量）</li>
 * </ul>
 *
 * <p>编码规则（X.691 §19）：
 * <ul>
 *   <li>零值：1 比特 (0)</li>
 *   <li>非零值：1 比特 (1) + 内容</li>
 *   <li>本实现采用 IEEE 754 双精度格式 (8 字节)，适用于 FLOAT32/FLOAT64</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   PerReal.encode(pos, 220.5);
 *   double voltage = PerReal.decode(pis);
 * </pre>
 */
public final class PerReal {

    private PerReal() { /* 工具类，不可实例化 */ }

    /**
     * 编码实数值（IEEE 754 双精度浮点格式）。
     *
     * @param pos 输出流
     * @param value 浮点值
     */
    public static void encode(PerOutputStream pos, double value) {
        if (value == 0.0) {
            pos.writeBit(false);
            return;
        }

        pos.writeBit(true);
        writeIeee754(pos, value);
    }

    /**
     * 解码实数值。
     *
     * @param pis 输入流
     * @return 解码出的双精度浮点值
     * @throws PerDecodeException 如果数据不足
     */
    public static double decode(PerInputStream pis) throws PerDecodeException {
        boolean isNonZero = pis.readBit();
        if (!isNonZero) {
            return 0.0;
        }
        return readIeee754(pis);
    }

    /**
     * 编码 32 位浮点值（IEEE 754 单精度）。
     *
     * @param pos 输出流
     * @param value 浮点值
     */
    public static void encodeFloat32(PerOutputStream pos, float value) {
        if (value == 0.0f) {
            pos.writeBit(false);
            return;
        }

        pos.writeBit(true);
        pos.align();
        int bits = Float.floatToIntBits(value);
        for (int i = 3; i >= 0; i--) {
            pos.writeByteAligned((byte) ((bits >> (i * 8)) & 0xFF));
        }
    }

    /**
     * 解码 32 位浮点值。
     *
     * @param pis 输入流
     * @return 解码出的单精度浮点值
     * @throws PerDecodeException 如果数据不足
     */
    public static float decodeFloat32(PerInputStream pis) throws PerDecodeException {
        boolean isNonZero = pis.readBit();
        if (!isNonZero) {
            return 0.0f;
        }

        pis.align();
        int bits = 0;
        for (int i = 0; i < 4; i++) {
            bits = (bits << 8) | (pis.readByteAligned() & 0xFF);
        }
        return Float.intBitsToFloat(bits);
    }

    private static void writeIeee754(PerOutputStream pos, double value) {
        long bits = Double.doubleToLongBits(value);
        pos.align();
        for (int i = 7; i >= 0; i--) {
            byte b = (byte) ((bits >> (i * 8)) & 0xFF);
            pos.writeByteAligned(b);
        }
    }

    private static double readIeee754(PerInputStream pis) throws PerDecodeException {
        pis.align();
        long bits = 0;
        for (int i = 0; i < 8; i++) {
            bits = (bits << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return Double.longBitsToDouble(bits);
    }
}
