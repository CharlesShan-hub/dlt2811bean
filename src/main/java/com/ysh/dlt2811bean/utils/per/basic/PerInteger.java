package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 INTEGER 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>INTEGER</b> (GB/T 16262 / X.680 §21)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>INT8U (0..255)、INT16U (0..65535)、INT32U (0..4294967295) — 无符号整数类型</li>
 *   <li>INT8 (-128..127)、INT16 (-32768..32767) — 有符号整数类型</li>
 *   <li>INT32U apduSize / asduSize (关联协商服务参数，表82)</li>
 *   <li>INT32U startPosition (文件读写服务参数)</li>
 * </ul>
 *
 * <p>编码规则（X.691 §12）：
 * <h3>有约束整数 (lb..ub)：</h3>
 * <ul>
 *   <li>编码偏移值 offset = value - lb</li>
 *   <li>范围 d = ub - lb + 1</li>
 *   <li>d=1：不编码（0 比特），收发双方都知道唯一可能值</li>
 *   <li>2 ≤ d ≤ 255：使用 ⌈log₂(d)⌉ 比特（紧凑编码）</li>
 *   <li>256 ≤ d ≤ 65536：使用 2 字节（对齐后）</li>
 *   <li>65537 ≤ d：使用 ⌈log₂(d)/8⌉ 字节（对齐后）</li>
 * </ul>
 *
 * <h3>半约束整数 (lb..MAX)：</h3>
 * <ul>
 *   <li>格式：[长度 L][内容字节]</li>
 *   <li>编码 (value - lb)，长度按 APER 长度规则</li>
 * </ul>
 *
 * <h3>无约束整数：</h3>
 * <ul>
 *   <li>格式：[长度 L][内容字节]</li>
 *   <li>值可为负数，使用补码形式</li>
 * </ul>
 *
 * <h3>小非负整数 (normally small number)：</h3>
 * <ul>
 *   <li>0~63：前导位 0 + 6 比特值（共 7 比特）</li>
 *   <li>≥64：前导位 1 + 半约束整数编码</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // INT16U apduSize (0..65535)
 *   PerInteger.encode(pos, 65535, 0, 65535);      // 2 字节
 *   int apduSize = (int) PerInteger.decode(pis, 0, 65535);
 *
 *   // 无约束整数
 *   PerInteger.encodeUnconstrained(pos, -100);
 *   long val = PerInteger.decodeUnconstrained(pis);
 *
 *   // 小非负整数（CHOICE 索引等）
 *   PerInteger.encodeSmallNonNegative(pos, 5);    // 7 比特
 *   long idx = PerInteger.decodeSmallNonNegative(pis);
 * </pre>
 */
public final class PerInteger {

    private PerInteger() { /* 工具类，不可实例化 */ }

    // ==================== 有约束整数 ====================

    /**
     * 编码有约束整数值。
     *
     * @param pos 输出流
     * @param value 整数值（必须在 [lowerBound, upperBound] 范围内）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @throws IllegalArgumentException 如果 value 超出约束范围
     */
    public static void encode(PerOutputStream pos, long value, long lowerBound, long upperBound) {
        if (value < lowerBound || value > upperBound) {
            throw new IllegalArgumentException(
                String.format("Value %d out of constrained range [%d, %d]", value, lowerBound, upperBound));
        }

        long range = upperBound - lowerBound + 1;
        if (range == 1) {
            // 唯一可能值，不编码任何比特
            return;
        }

        if (range <= 256) {
            // 使用 ceil(log2(range)) 比特编码偏移值
            int bitsNeeded = calculateBitsNeeded(range);
            long offset = value - lowerBound;
            pos.writeBits(offset, bitsNeeded);
        } else if (range <= 65536) {
            // APER: 对齐后用 2 字节编码
            long offset = value - lowerBound;
            pos.align();
            pos.writeBits(offset, 16);
        } else {
            // 范围很大：需要确定字节数并编码
            long offset = value - lowerBound;
            int bytesNeeded = calculateBytesForRange(range);
            // 先对齐，再写入指定字节数的偏移值
            pos.align();
            for (int i = bytesNeeded - 1; i >= 0; i--) {
                byte b = (byte) ((offset >> (i * 8)) & 0xFF);
                pos.writeByteAligned(b);
            }
        }
    }

    /**
     * 解码有约束整数值。
     *
     * @param pis 输入流
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 解码出的整数值
     * @throws PerDecodeException 如果数据不足
     */
    public static long decode(PerInputStream pis, long lowerBound, long upperBound) throws PerDecodeException {
        long range = upperBound - lowerBound + 1;
        if (range == 1) {
            return lowerBound; // 唯一可能的值
        }

        if (range <= 256) {
            int bitsNeeded = calculateBitsNeeded(range);
            long offset = pis.readBits(bitsNeeded);
            return lowerBound + offset;
        } else if (range <= 65536) {
            pis.align();
            long offset = pis.readBits(16);
            return lowerBound + offset;
        } else {
            int bytesNeeded = calculateBytesForRange(range);
            pis.align();
            long offset = 0;
            for (int i = 0; i < bytesNeeded; i++) {
                offset = (offset << 8) | (pis.readByteAligned() & 0xFFL);
            }
            return lowerBound + offset;
        }
    }

    // ==================== 小非负整数（用于 CHOICE 索引、ENUMERATED 序号等） ====================

    /**
     * 编码非负的小整数（normally small non-negative integer）。
     *
     * <p>按照 X.691 的 "normally small non-negative whole number" 编码：
     * <ul>
     *   <li>0 ~ 63：前导位 0 + 6 比特值（共 7 比特）</li>
     *   <li>&gt;= 64：前导位 1 + 半约束整数编码</li>
     * </ul>
     *
     * @param pos 输出流
     * @param value 非负整数值
     */
    public static void encodeSmallNonNegative(PerOutputStream pos, int value) {
        if (value >= 0 && value <= 63) {
            // 前 7 位：[0][6-bit value]
            pos.writeBit(false);       // 扩展标记 = 0（小值）
            pos.writeBits(value, 6);
        } else {
            // 大值：前导位 1 + 半约束编码
            pos.writeBit(true);        // 扩展标记 = 1（大值）
            encodeSemiConstrained(pos, (long) value, 0);
        }
    }

    /**
     * 解码非负小整数。
     *
     * @param pis 输入流
     * @return 解码出的非负整数值
     * @throws PerDecodeException 如果数据不足
     */
    public static long decodeSmallNonNegative(PerInputStream pis) throws PerDecodeException {
        boolean isLarge = pis.readBit(); // 扩展标记
        if (!isLarge) {
            return pis.readBits(6); // 小值：直接读 6 比特
        } else {
            return decodeSemiConstrained(pis, 0); // 大值：半约束解码
        }
    }

    // ==================== 半约束/无约束整数 ====================

    /**
     * 编码半约束整数值 (lb..MAX)。
     *
     * <p>格式：[长度 L][内容（对齐后的字节）]
     *
     * @param pos 输出流
     * @param value 整数值
     * @param lowerBound 下界（可为 0 表示纯无约束）
     */
    public static void encodeSemiConstrained(PerOutputStream pos, long value, long lowerBound) {
        long offset = value - lowerBound;
        byte[] content = encodeUnsignedValueToBytes(offset);

        // 写入内容长度（按 APER 长度规则）
        encodeLength(pos, content.length);

        // 写入内容（对齐后逐字节）
        pos.writeBytes(content);
    }

    /**
     * 解码半约束整数值 (lb..MAX)。
     *
     * @param pis 输入流
     * @param lowerBound 下界
     * @return 解码出的整数值
     * @throws PerDecodeException 如果数据不足
     */
    public static long decodeSemiConstrained(PerInputStream pis, long lowerBound) throws PerDecodeException {
        // 读内容长度
        int length = decodeLength(pis);

        // 读内容字节
        byte[] content = pis.readBytes(length);

        // 将字节转为长整数值
        long offset = bytesToUnsignedLong(content);
        return lowerBound + offset;
    }

    /**
     * 编码无约束整数值（无上下界限制）。
     *
     * <p>格式同半约束，但值可以为负数。使用 BER 兼容的补码形式。
     *
     * @param pos 输出流
     * @param value 整数值（可正可负）
     */
    public static void encodeUnconstrained(PerOutputStream pos, long value) {
        byte[] content = encodeSignedValueToBytes(value);
        encodeLength(pos, content.length);
        pos.writeBytes(content);
    }

    /**
     * 解码无约束整数值。
     *
     * @param pis 输入流
     * @return 解码出的整数值
     * @throws PerDecodeException 如果数据不足
     */
    public static long decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = decodeLength(pis);
        byte[] content = pis.readBytes(length);
        return bytesToSignedLong(content);
    }

    // ==================== 长度编解码（APER 规范） ====================

    /**
     * 按 APER 规则编码长度字段。
     *
     * <p>X.691 §11.9 长度确定规则：
     * <ul>
     *   <li>0 ~ 127：单字节，MSB=0，低 7 位为长度</li>
     *   <li>128 ~ 16383：双字节，MSB=10，14 位为长度</li>
     *   <li>&gt;= 16384：多段编码，MSB=11，每段 16K</li>
     * </ul>
     *
     * @param pos 输出流
     * @param length 要编码的长度值（&gt;= 0）
     */
    public static void encodeLength(PerOutputStream pos, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be >= 0");
        }

        if (length <= 127) {
            // 短形式：0xxxxxxx
            pos.writeByteAligned((byte) length);
        } else if (length <= 16383) {
            // 中等形式：10xxxxxxxxxxxxxx
            byte high = (byte) ((length >> 8) | 0x40); // MSB=10
            byte low = (byte) (length & 0xFF);
            pos.writeByteAligned(high);
            pos.writeByteAligned(low);
        } else {
            // 长形式：11 后跟分段计数
            int fragments = (length + 16383) / 16384;
            byte header = (byte) ((fragments << 4) | 0xC0); // MSB=11
            pos.writeByteAligned(header);

            int remaining = length;
            for (int i = 0; i < fragments; i++) {
                int segmentLen = Math.min(remaining, 16384);
                pos.writeByteAligned((byte) (segmentLen >> 8));
                pos.writeByteAligned((byte) (segmentLen & 0xFF));
                remaining -= segmentLen;
            }
        }
    }

    /**
     * 按 APER 规则解码长度字段。
     *
     * @param pis 输入流
     * @return 解码出的长度值
     * @throws PerDecodeException 如果数据不足
     */
    public static int decodeLength(PerInputStream pis) throws PerDecodeException {
        pis.align();

        int firstByte = pis.readByteAligned() & 0xFF;

        if ((firstByte & 0x80) == 0) {
            // 短形式：0xxxxxxx → 长度 0~127
            return firstByte;
        }

        if ((firstByte & 0xC0) == 0x40) {
            // 中等形式：10xxxxxxxxxxxxxx
            int secondByte = pis.readByteAligned() & 0xFF;
            return ((firstByte & 0x3F) << 8) | secondByte;
        }

        // 长形式：11xxxxxxxxxxxx
        int numFragments = (firstByte >> 4) & 0x07;
        int totalLength = 0;
        for (int i = 0; i < numFragments; i++) {
            int hi = pis.readByteAligned() & 0xFF;
            int lo = pis.readByteAligned() & 0xFF;
            totalLength = totalLength + ((hi << 8) | lo);
        }
        return totalLength;
    }

    // ==================== 内部工具方法 ====================

    /** 计算 range 所需的比特数（向上取整 log2）。 */
    static int calculateBitsNeeded(long range) {
        if (range <= 1) return 0;
        return Long.SIZE - Long.numberOfLeadingZeros(range - 1);
    }

    /** 计算大范围值所需的字节数。 */
    static int calculateBytesForRange(long range) {
        if (range <= 65536) return 2;
        long maxOffset = range - 1;
        if (maxOffset <= 0xFFFFFFL) return 3;
        if (maxOffset <= 0xFFFFFFFFL) return 4;
        if (maxOffset <= 0xFFFFFFFFFFL) return 5;
        if (maxOffset <= 0xFFFFFFFFFFFFL) return 6;
        if (maxOffset <= 0xFFFFFFFFFFFFFFL) return 7;
        return 8;
    }

    /** 将无符号长整数值编码为最小所需字节数组（大端序）。 */
    static byte[] encodeUnsignedValueToBytes(long value) {
        if (value == 0) return new byte[]{0};
        int bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(value) + 7) / 8;
        byte[] result = new byte[bytesNeeded];
        for (int i = bytesNeeded - 1; i >= 0; i--) {
            result[bytesNeeded - 1 - i] = (byte) ((value >> (i * 8)) & 0xFF);
        }
        return result;
    }

    /** 将有符号长整数值编码为 BER 兼容的最小字节数组（补码，大端序）。 */
    static byte[] encodeSignedValueToBytes(long value) {
        if (value == 0) return new byte[]{0};

        int bytesNeeded;
        if (value > 0) {
            bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(value) + 8) / 8;
        } else {
            bytesNeeded = (Long.SIZE - Long.numberOfLeadingZeros(~value) + 8) / 8;
        }

        byte[] result = new byte[bytesNeeded];
        for (int i = 0; i < bytesNeeded; i++) {
            int shift = (bytesNeeded - 1 - i) * 8;
            result[i] = (byte) ((value >> shift) & 0xFF);
        }
        return result;
    }

    /** 从大端字节数组解析无符号长整数。 */
    static long bytesToUnsignedLong(byte[] data) {
        long result = 0;
        for (byte b : data) {
            result = (result << 8) | (b & 0xFFL);
        }
        return result;
    }

    /** 从大端字节数组解析有符号长整数（补码）。 */
    static long bytesToSignedLong(byte[] data) {
        if (data.length == 0) return 0;
        long result = data[0] & 0xFFL;
        if ((data[0] & 0x80) != 0) {
            result |= (~0L) << 8;
        }
        for (int i = 1; i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFFL);
        }
        return result;
    }
}
