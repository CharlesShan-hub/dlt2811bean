package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ASN.1 VisibleString / IA5String 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>VisibleString</b> (GB/T 16262 / X.680 §40) / <b>IA5String</b> (X.680 §39)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>VisibleString(129) — 访问点引用（表19，serverAccessPointReference）</li>
 *   <li>VisibleString(255) — 文件名（表73，fileName）</li>
 *   <li>VisibleString — 对象引用 ObjectReference（7.3.4，LD/LN 引用路径）</li>
 *   <li>VISIBLESTRING — 方法引用（表81，method）</li>
 * </ul>
 *
 * <p>编码规则（X.691 §41）：
 * <h3>固定长度 SIZE(n)：</h3>
 * <ul>
 *   <li>每个字符 8 比特（无字符集约束时）</li>
 *   <li>有 FROM 约束时每个字符 ⌈log₂(|charset|)⌉ 比特</li>
 * </ul>
 *
 * <h3>变长 SIZE(lb..ub)：</h3>
 * <ul>
 *   <li>先编码字符数长度</li>
 *   <li>再编码各字符</li>
 * </ul>
 *
 * <h3>无约束：</h3>
 * <ul>
 *   <li>按长度规则编码字节数</li>
 *   <li>再编码各字节</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // 固定 129 字符 — serverAccessPointReference
 *   PerVisibleString.encodeFixedSize(pos, "S1.AccessPoint1", 129);
 *   String ref = PerVisibleString.decodeFixedSize(pis, 129);
 *
 *   // 变长 — ObjectReference
 *   PerVisibleString.encodeConstrained(pos, "LD1/LN0.DO1", 0, 255);
 *   String objRef = PerVisibleString.decodeConstrained(pis, 0, 255);
 * </pre>
 */
public final class PerVisibleString {

    /** 默认编码：ISO 8859-1（VisibleString 使用 ISO 646 字符集，兼容 ASCII） */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;

    private PerVisibleString() { /* 工具类，不可实例化 */ }

    // ==================== 固定长度 + 无字符集约束 ====================

    /**
     * 编码固定长度的可见字符串（无字符集约束，每字符 8 比特）。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param fixedSize 固定字符数
     */
    public static void encodeFixedSize(PerOutputStream pos, String value, int fixedSize) {
        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        int writeLen = Math.min(bytes.length, fixedSize);
        for (int i = 0; i < writeLen; i++) {
            pos.writeBits(bytes[i] & 0xFF, 8);
        }
        for (int i = writeLen; i < fixedSize; i++) {
            pos.writeBits(0x20, 8); // 填充空格
        }
    }

    /**
     * 解码固定长度的可见字符串（无字符集约束）。
     *
     * @param pis 输入流
     * @param fixedSize 固定字符数
     * @return 解码出的字符串（已 trim 尾部空格）
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return "";
        byte[] bytes = new byte[fixedSize];
        for (int i = 0; i < fixedSize; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET).trim();
    }

    // ==================== 固定长度 + 字符集约束 (FROM) ====================

    /**
     * 编码固定长度的可见字符串（有 FROM 字符集约束）。
     *
     * <p>每个字符用 ⌈log₂(charsetSize)⌉ 比特编码。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param fixedSize 固定字符数
     * @param charsetTable 允许的字符集合
     */
    public static void encodeFixedSizeConstrained(
            PerOutputStream pos, String value, int fixedSize, String charsetTable) {

        int bitsPerChar = calculateBitsPerChar(charsetTable.length());

        for (int i = 0; i < fixedSize; i++) {
            char ch = (i < value.length()) ? value.charAt(i) : ' ';
            int index = charsetTable.indexOf(ch);
            if (index < 0) index = charsetTable.indexOf(' ');
            if (index < 0) index = 0;
            pos.writeBits(index, bitsPerChar);
        }
    }

    /**
     * 解码固定长度的可见字符串（有 FROM 字符集约束）。
     *
     * @param pis 输入流
     * @param fixedSize 固定字符数
     * @param charsetTable 允许的字符集合
     * @return 解码出的字符串（已 trim）
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeFixedSizeConstrained(
            PerInputStream pis, int fixedSize, String charsetTable) throws PerDecodeException {

        if (fixedSize == 0) return "";
        int bitsPerChar = calculateBitsPerChar(charsetTable.length());
        StringBuilder sb = new StringBuilder(fixedSize);

        for (int i = 0; i < fixedSize; i++) {
            long index = pis.readBits(bitsPerChar);
            char ch = (index >= 0 && index < charsetTable.length())
                ? charsetTable.charAt((int) index)
                : '?';
            sb.append(ch);
        }
        return sb.toString().trim();
    }

    // ==================== 变长 ====================

    /**
     * 编码变长可见字符串（无字符集约束）。
     *
     * @param pos 输出流
     * @param value 字符串值
     * @param lowerBound 最小长度
     * @param upperBound 最大长度
     */
    public static void encodeConstrained(PerOutputStream pos, String value,
                                         int lowerBound, int upperBound) {
        int length = value != null ? value.length() : 0;
        PerInteger.encode(pos, length, lowerBound, upperBound);

        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    /**
     * 解码变长可见字符串（无字符集约束）。
     *
     * @param pis 输入流
     * @param lowerBound 最小长度
     * @param upperBound 最大长度
     * @return 解码出的字符串
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {

        int length = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (length == 0) return "";

        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }

    /**
     * 编码无约束/半约束可见字符串。
     *
     * @param pos 输出流
     * @param value 字符串值
     */
    public static void encodeUnconstrained(PerOutputStream pos, String value) {
        if (value == null || value.isEmpty()) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        byte[] bytes = value.getBytes(DEFAULT_CHARSET);
        PerInteger.encodeLength(pos, bytes.length);
        for (byte b : bytes) {
            pos.writeBits(b & 0xFF, 8);
        }
    }

    /**
     * 解码无约束/半约束可见字符串。
     *
     * @param pis 输入流
     * @return 解码出的字符串
     * @throws PerDecodeException 如果数据不足
     */
    public static String decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return "";

        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) pis.readBits(8);
        }
        return new String(bytes, DEFAULT_CHARSET);
    }

    // ==================== 内部工具 ====================

    private static int calculateBitsPerChar(int charsetSize) {
        if (charsetSize <= 1) return 0;
        return Integer.SIZE - Integer.numberOfLeadingZeros(charsetSize - 1);
    }
}
