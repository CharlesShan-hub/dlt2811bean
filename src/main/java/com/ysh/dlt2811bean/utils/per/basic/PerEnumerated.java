package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 ENUMERATED 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>ENUMERATED</b> (GB/T 16262 / X.680 §20)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>objectClass ENUMERATED { logical-device(1) } (表23)</li>
 *   <li>ACSIClass ENUMERATED (表9)</li>
 *   <li>AddCause CODEDENUM (表15, 定长位串形式)</li>
 * </ul>
 *
 * <p>编码规则（X.691 §14）：
 * <ul>
 *   <li><b>不可扩展</b>：枚举值按定义顺序编号为 0, 1, 2, ...，按有约束整数 (0..maxIndex) 编码序号</li>
 *   <li><b>可扩展 (...)</b>：前导比特 P（0=根部分, 1=扩展附加部分），根部分同不可扩展，扩展部分按小非负整数编码</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // 不可扩展枚举: objectClass ENUMERATED { reserved(0), logical-device(1) }
 *   PerEnumerated.encode(pos, 1, 1);     // maxOrdinal=1 (两个值: 0,1)
 *   int ordinal = PerEnumerated.decode(pis, 1);
 *
 *   // 可扩展枚举
 *   PerEnumerated.encodeExtensible(pos, false, 1, 2);  // 根部分，序号1，根最大序号2
 *   EnumeratedResult r = PerEnumerated.decodeExtensible(pis, 2);
 * </pre>
 */
public final class PerEnumerated {

    private PerEnumerated() { /* 工具类，不可实例化 */ }

    /**
     * 编码不可扩展的枚举值。
     *
     * @param pos        输出流
     * @param ordinal    枚举序号（从 0 开始）
     * @param maxOrdinal 最大枚举序号（即枚举总数 - 1）
     */
    public static void encode(PerOutputStream pos, int ordinal, int maxOrdinal) {
        if (ordinal < 0 || ordinal > maxOrdinal) {
            throw new IllegalArgumentException(
                String.format("Enum ordinal %d out of range [0, %d]", ordinal, maxOrdinal));
        }
        PerInteger.encode(pos, ordinal, 0, maxOrdinal);
    }

    /**
     * 解码不可扩展的枚举值。
     *
     * @param pis        输入流
     * @param maxOrdinal 最大枚举序号
     * @return 枚举序号
     * @throws PerDecodeException 如果数据不足
     */
    public static int decode(PerInputStream pis, int maxOrdinal) throws PerDecodeException {
        return (int) PerInteger.decode(pis, 0, maxOrdinal);
    }

    /**
     * 编码可扩展的枚举值。
     *
     * @param pos            输出流
     * @param isExtension    true=使用扩展附加部分
     * @param ordinal        枚举序号
     * @param rootMaxOrdinal 根部分最大序号
     */
    public static void encodeExtensible(PerOutputStream pos,
                                        boolean isExtension, int ordinal, int rootMaxOrdinal) {
        pos.writeBit(isExtension);
        if (!isExtension) {
            PerInteger.encode(pos, ordinal, 0, rootMaxOrdinal);
        } else {
            PerInteger.encodeSmallNonNegative(pos, ordinal);
        }
    }

    /**
     * 解码可扩展的枚举值。
     *
     * @param pis            输入流
     * @param rootMaxOrdinal 根部分最大序号
     * @return 包含是否扩展和序号的结果
     * @throws PerDecodeException 如果数据不足
     */
    public static EnumeratedResult decodeExtensible(PerInputStream pis, int rootMaxOrdinal)
            throws PerDecodeException {

        boolean isExtension = pis.readBit();
        if (isExtension) {
            long extOrdinal = PerInteger.decodeSmallNonNegative(pis);
            return new EnumeratedResult(true, (int) extOrdinal);
        } else {
            int ordinal = (int) PerInteger.decode(pis, 0, rootMaxOrdinal);
            return new EnumeratedResult(false, ordinal);
        }
    }

    /** 可扩展枚举的解码结果。 */
    public static class EnumeratedResult {
        /** 是否使用了扩展附加部分。 */
        public final boolean isExtension;
        /** 枚举序号。 */
        public final int ordinal;

        public EnumeratedResult(boolean isExtension, int ordinal) {
            this.isExtension = isExtension;
            this.ordinal = ordinal;
        }
    }
}
