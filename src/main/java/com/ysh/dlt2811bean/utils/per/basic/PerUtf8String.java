package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

import java.nio.charset.StandardCharsets;

/**
 * ASN.1 UTF8String / BMPString / UniversalString 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>UTF8String</b> (GB/T 16262 / X.680 §42) /
 * <b>BMPString</b> (X.680 §41) / <b>UniversalString</b> (X.680 §40)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>UNICODE STRING — Unicode 字符串数据值（7.1.5，表6 数据串类型）</li>
 *   <li>实际使用较少，主要用于支持非 ASCII 字符的设备名称或描述</li>
 * </ul>
 *
 * <p>编码规则（X.691 §42）：
 * <ul>
 *   <li>UTF8String：字符串先转为 UTF-8 字节序列，然后按 OCTET STRING 规则编码</li>
 *   <li>BMPString：每个字符 2 字节（UCS-2），按 OCTET STRING 规则编码</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // UTF-8 字符串（变长）
 *   PerUtf8String.encodeUtf8(pos, "设备名称");
 *   String name = PerUtf8String.decodeUtf8(pis);
 *
 *   // UTF-8 字符串（有约束范围）
 *   PerUtf8String.encodeUtf8Constrained(pos, "描述文本", 0, 255);
 *   String desc = PerUtf8String.decodeUtf8Constrained(pis, 0, 255);
 * </pre>
 */
public final class PerUtf8String {

    private PerUtf8String() { /* 工具类，不可实例化 */ }

    // ==================== UTF8String ====================

    /**
     * 编码 UTF8String（无约束）。
     *
     * @param pos 输出流
     * @param value 字符串值
     */
    public static void encodeUtf8(PerOutputStream pos, String value) {
        if (value == null || value.isEmpty()) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        PerInteger.encodeLength(pos, bytes.length);
        pos.writeBytes(bytes);
    }

    /**
     * 解码 UTF8String（无约束）。
     *
     * @param pis 输入流
     * @return 解码出的字符串
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeUtf8(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";

        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 编码 UTF8String（有约束范围）。
     *
     * <p>注意：约束范围针对的是 UTF-8 编码后的字节数，不是字符数。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param lowerBound 最小字节数
     * @param upperBound 最大字节数
     * @throws IllegalArgumentException 如果 UTF-8 编码后的字节数超出约束
     */
    public static void encodeUtf8Constrained(PerOutputStream pos, String value,
                                             int lowerBound, int upperBound) {
        byte[] bytes = value != null ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
        int actualLength = bytes.length;

        if (actualLength < lowerBound || actualLength > upperBound) {
            throw new IllegalArgumentException(
                String.format("UTF8String byte length %d out of range [%d, %d]",
                    actualLength, lowerBound, upperBound));
        }

        PerInteger.encode(pos, actualLength, lowerBound, upperBound);
        if (actualLength > 0) {
            pos.writeBytes(bytes);
        }
    }

    /**
     * 解码 UTF8String（有约束范围）。
     *
     * @param pis 输入流
     * @param lowerBound 最小字节数
     * @param upperBound 最大字节数
     * @return 解码出的字符串
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeUtf8Constrained(PerInputStream pis,
                                               int lowerBound, int upperBound) throws PerDecodeException {
        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";

        byte[] bytes = pis.readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ==================== BMPString (UCS-2, 每字符 2 字节) ====================

    /**
     * 编码 BMPString（固定长度，每字符 2 字节 UCS-2 大端序）。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param fixedCharCount 固定字符数
     */
    public static void encodeBmpFixedSize(PerOutputStream pos, String value, int fixedCharCount) {
        pos.align();
        for (int i = 0; i < fixedCharCount; i++) {
            char ch = (i < value.length()) ? value.charAt(i) : ' ';
            pos.writeByteAligned((byte) (ch >> 8));
            pos.writeByteAligned((byte) (ch & 0xFF));
        }
    }

    /**
     * 解码 BMPString（固定长度）。
     *
     * @param pis 输入流
     * @param fixedCharCount 固定字符数
     * @return 解码出的字符串（已 trim）
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeBmpFixedSize(PerInputStream pis, int fixedCharCount)
            throws PerDecodeException {
        if (fixedCharCount == 0) return "";

        pis.align();
        byte[] bytes = pis.readBytes(fixedCharCount * 2);
        StringBuilder sb = new StringBuilder(fixedCharCount);
        for (int i = 0; i < fixedCharCount * 2; i += 2) {
            char ch = (char) (((bytes[i] & 0xFF) << 8) | (bytes[i + 1] & 0xFF));
            sb.append(ch);
        }
        return sb.toString().trim();
    }

    /**
     * 编码 BMPString（变长）。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param lowerBound 最小字符数
     * @param upperBound 最大字符数
     */
    public static void encodeBmpConstrained(PerOutputStream pos, String value,
                                            int lowerBound, int upperBound) {
        int charCount = value != null ? value.length() : 0;
        PerInteger.encode(pos, charCount, lowerBound, upperBound);

        pos.align();
        for (int i = 0; i < charCount; i++) {
            char ch = value.charAt(i);
            pos.writeByteAligned((byte) (ch >> 8));
            pos.writeByteAligned((byte) (ch & 0xFF));
        }
    }

    /**
     * 解码 BMPString（变长）。
     *
     * @param pis 输入流
     * @param lowerBound 最小字符数
     * @param upperBound 最大字符数
     * @return 解码出的字符串
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeBmpConstrained(PerInputStream pis,
                                              int lowerBound, int upperBound) throws PerDecodeException {
        int charCount = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (charCount == 0) return "";

        pis.align();
        byte[] bytes = pis.readBytes(charCount * 2);
        StringBuilder sb = new StringBuilder(charCount);
        for (int i = 0; i < charCount * 2; i += 2) {
            char ch = (char) (((bytes[i] & 0xFF) << 8) | (bytes[i + 1] & 0xFF));
            sb.append(ch);
        }
        return sb.toString();
    }
}
