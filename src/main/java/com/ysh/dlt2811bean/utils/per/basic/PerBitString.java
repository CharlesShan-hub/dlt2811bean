package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 BIT STRING 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>BIT STRING</b> (GB/T 16262 / X.680 §22)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>TriggerConditions CODEDENUM — 定长位串 6 比特（表7，触发条件）</li>
 *   <li>ReasonCode CODEDENUM — 定长位串 7 比特（7.6.3，触发原因）</li>
 *   <li>RCBOptFlds — 定长位串 10 比特（表16，报告控制块选项域）</li>
 *   <li>MSVCBOptFlds — 定长位串 5 比特（表17，多播采样值选项域）</li>
 *   <li>SmpMod CODEDENUM — 定长位串 2 比特（表18，采样模式）</li>
 *   <li>TimeQuality — 定长位串（7.2.3，时间品质）</li>
 *   <li>Packedlist — 变长位串（7.2.5，紧凑列表）</li>
 * </ul>
 *
 * <p>编码规则（X.691 §16）：
 * <h3>固定长度 SIZE(n)：</h3>
 * <ul>
 *   <li>n ≤ 16 比特：直接编码 n 个比特（无对齐）</li>
 *   <li>17 ≤ n ≤ 65536 比特：字节对齐后直接编码</li>
 *   <li>n &gt; 65536：先编码长度再编码内容</li>
 * </ul>
 *
 * <h3>变长 SIZE(lb..ub)：</h3>
 * <ul>
 *   <li>先编码实际比特长度（按有约束整数规则）</li>
 *   <li>再编码比特内容（对齐后）</li>
 * </ul>
 *
 * <h3>无约束：</h3>
 * <ul>
 *   <li>先编码字节长度（按长度规则）</li>
 *   <li>再编码内容字节 + 末尾 unused-bits 计数</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // TriggerConditions — 6 比特定长位串
 *   PerBitString.encodeFixedSize(pos, 0b000110, 6);
 *   long trigger = PerBitString.decodeFixedSize(pis, 6);
 *
 *   // RCBOptFlds — 10 比特定长位串
 *   PerBitString.encodeFixedSize(pos, fields, 10);
 *
 *   // 变长位串
 *   PerBitString.encodeConstrained(pos, data, actualBits, 0, 65535);
 *   byte[] result = PerBitString.decodeConstrained(pis, 0, 65535);
 * </pre>
 */
public final class PerBitString {

    private PerBitString() { /* 工具类，不可实例化 */ }

    // ==================== 固定长度 ====================

    /**
     * 编码固定长度的位串（long 形式，适用于 ≤64 比特）。
     *
     * @param pos 输出流
     * @param value 位串值（只使用低 fixedSize 位）
     * @param fixedSize 固定比特长度
     */
    public static void encodeFixedSize(PerOutputStream pos, long value, int fixedSize) {
        if (fixedSize == 0) return;

        if (fixedSize <= 16) {
            pos.writeBits(value, fixedSize);
        } else if (fixedSize <= 65536) {
            pos.align();
            int bytesToWrite = (fixedSize + 7) / 8;
            for (int i = bytesToWrite - 1; i >= 0; i--) {
                byte b = (byte) ((value >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        } else {
            PerInteger.encodeLength(pos, (fixedSize + 7) / 8);
            pos.align();
            int bytesToWrite = (fixedSize + 7) / 8;
            for (int i = bytesToWrite - 1; i >= 0; i--) {
                byte b = (byte) ((value >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        }
    }

    /**
     * 解码固定长度的位串（long 形式，适用于 ≤64 比特）。
     *
     * @param pis 输入流
     * @param fixedSize 固定比特长度
     * @return 位串值（存储在 long 的低位中）
     * @throws PerDecodeException 如果数据不足
     */
    public static long decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return 0;

        if (fixedSize <= 16) {
            return pis.readBits(fixedSize);
        } else if (fixedSize <= 65536) {
            pis.align();
            int bytesToRead = (fixedSize + 7) / 8;
            return readBytesAsLong(pis, bytesToRead);
        } else {
            int length = PerInteger.decodeLength(pis);
            pis.align();
            return readBytesAsLong(pis, length);
        }
    }

    /**
     * 编码固定长度位串为字节数组形式（适用于超过 64 比特的情况）。
     *
     * @param pos 输出流
     * @param data 字节数组形式的位数据
     * @param totalBits 总比特数
     */
    public static void encodeFixedSize(PerOutputStream pos, byte[] data, int totalBits) {
        if (totalBits == 0 || (data != null && data.length == 0)) return;

        if (totalBits <= 16) {
            long value = bytesToLongBits(data, totalBits);
            pos.writeBits(value, totalBits);
        } else {
            pos.align();
            int bytesNeeded = (totalBits + 7) / 8;
            for (int i = 0; i < bytesNeeded && i < data.length; i++) {
                pos.writeByteAligned(data[i]);
            }
            for (int i = data.length; i < bytesNeeded; i++) {
                pos.writeByteAligned((byte) 0);
            }
        }
    }

    /**
     * 解码固定长度位串到字节数组。
     *
     * @param pis 输入流
     * @param totalBits 总比特数
     * @return 包含位数据的字节数组
     * @throws PerDecodeException 如果数据不足
     */
    public static byte[] decodeFixedSizeBytes(PerInputStream pis, int totalBits) throws PerDecodeException {
        if (totalBits == 0) return new byte[0];

        if (totalBits <= 16) {
            long value = pis.readBits(totalBits);
            return longBitsToBytes(value, totalBits);
        }

        pis.align();
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = 0; i < bytesNeeded; i++) {
            result[i] = (byte) pis.readByteAligned();
        }
        return result;
    }

    // ==================== 变长（有约束范围） ====================

    /**
     * 编码变长位串（有 SIZE 约束范围 lb..ub）。
     *
     * @param pos 输出流
     * @param data 位数据字节数组
     * @param actualBits 实际比特数
     * @param lowerBound 最小比特数
     * @param upperBound 最大比特数
     */
    public static void encodeConstrained(
            PerOutputStream pos, byte[] data, int actualBits,
            int lowerBound, int upperBound) {

        PerInteger.encode(pos, actualBits, lowerBound, upperBound);

        int bytesToWrite = (actualBits + 7) / 8;
        pos.align();
        for (int i = 0; i < bytesToWrite && i < data.length; i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = data.length; i < bytesToWrite; i++) {
            pos.writeByteAligned((byte) 0);
        }
    }

    /**
     * 解码变长位串。
     *
     * @param pis 输入流
     * @param lowerBound 最小比特数
     * @param upperBound 最大比特数
     * @return 位数据字节数组
     * @throws PerDecodeException 如果数据不足
     */
    public static byte[] decodeConstrained(PerInputStream pis, int lowerBound, int upperBound)
            throws PerDecodeException {

        int actualBits = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (actualBits == 0) return new byte[0];

        pis.align();
        int bytesToRead = (actualBits + 7) / 8;
        byte[] result = new byte[bytesToRead];
        for (int i = 0; i < bytesToRead; i++) {
            result[i] = (byte) pis.readByteAligned();
        }
        return result;
    }

    // ==================== 无约束 ====================

    /**
     * 编码无约束位串（格式：[字节长度][内容字节][unused-bits-count]）。
     *
     * @param pos 输出流
     * @param data 位数据
     * @param totalBits 有效比特数
     */
    public static void encodeUnconstrained(PerOutputStream pos, byte[] data, int totalBits) {
        int contentBytes = (totalBits + 7) / 8;
        int unusedBits = contentBytes * 8 - totalBits;

        // 内容总长度 = 数据字节 + 1 字节的 unused-bits 计数
        PerInteger.encodeLength(pos, contentBytes + 1);
        pos.align();

        for (int i = 0; i < contentBytes && (data != null && i < data.length); i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = (data != null ? Math.min(data.length, contentBytes) : 0); i < contentBytes; i++) {
            pos.writeByteAligned((byte) 0);
        }

        pos.writeByteAligned((byte) unusedBits);
    }

    /**
     * 解码无约束位串。
     *
     * @param pis 输入流
     * @return 位串结果（包含数据和有效比特数）
     * @throws PerDecodeException 如果数据不足
     */
    public static BitStringResult decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int totalLength = PerInteger.decodeLength(pis);
        pis.align();

        byte[] raw = new byte[totalLength];
        for (int i = 0; i < totalLength; i++) {
            raw[i] = (byte) pis.readByteAligned();
        }

        int unusedBits = raw[totalLength - 1] & 0xFF;
        int contentBytes = totalLength - 1;
        int totalBits = contentBytes * 8 - unusedBits;

        byte[] data = new byte[contentBytes];
        System.arraycopy(raw, 0, data, 0, contentBytes);

        return new BitStringResult(data, totalBits);
    }

    // ==================== 内部工具方法 ====================

    private static long readBytesAsLong(PerInputStream pis, int bytesToRead) throws PerDecodeException {
        long result = 0;
        for (int i = 0; i < bytesToRead; i++) {
            result = (result << 8) | (pis.readByteAligned() & 0xFFL);
        }
        return result;
    }

    private static long bytesToLongBits(byte[] data, int totalBits) {
        long result = 0;
        int bytesToUse = (totalBits + 7) / 8;
        for (int i = 0; i < bytesToUse && i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFFL);
        }
        if (totalBits % 8 != 0) {
            long mask = (1L << totalBits) - 1;
            result &= mask;
        }
        return result;
    }

    private static byte[] longBitsToBytes(long value, int totalBits) {
        int bytesNeeded = (totalBits + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }

    // ==================== 结果封装 ====================

    /** 无约束位串的解码结果。 */
    public static class BitStringResult {
        /** 位数据。 */
        public final byte[] data;
        /** 有效比特数。 */
        public final int bitLength;

        public BitStringResult(byte[] data, int bitLength) {
            this.data = data;
            this.bitLength = bitLength;
        }
    }
}
