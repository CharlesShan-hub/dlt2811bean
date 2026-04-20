package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 OCTET STRING 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>OCTET STRING</b> (GB/T 16262 / X.680 §23)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>OCTET STRING(64) — 关联标识（表19，associationId）</li>
 *   <li>OCTET STRING — 安全认证参数（表19，authenticationParameter）</li>
 *   <li>OCTET STRING — 文件数据（表73，fileData）</li>
 *   <li>OCTET STRING — 签名证书、签名值（应用层安全参数）</li>
 * </ul>
 *
 * <p>编码规则（X.691 §17）：
 * <h3>固定长度 SIZE(n)：</h3>
 * <ul>
 *   <li>对齐后直接编码 n 个字节</li>
 *   <li>无长度字段</li>
 * </ul>
 *
 * <h3>变长 SIZE(lb..ub)：</h3>
 * <ul>
 *   <li>先编码实际字节长度（按有约束整数规则，偏移 = length - lb）</li>
 *   <li>再编码内容字节（对齐后）</li>
 * </ul>
 *
 * <h3>无约束：</h3>
 * <ul>
 *   <li>按长度规则编码字节数 L</li>
 *   <li>再编码 L 个字节的内容</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // 固定 64 字节 — associationId
 *   PerOctetString.encodeFixedSize(pos, assocIdBytes, 64);
 *   byte[] assocId = PerOctetString.decodeFixedSize(pis, 64);
 *
 *   // 变长 OCTET STRING — authenticationParameter
 *   PerOctetString.encodeConstrained(pos, certBytes, 0, 8192);
 *   byte[] cert = PerOctetString.decodeConstrained(pis, 0, 8192);
 *
 *   // 无约束
 *   PerOctetString.encodeUnconstrained(pos, fileData);
 *   byte[] data = PerOctetString.decodeUnconstrained(pis);
 * </pre>
 */
public final class PerOctetString {

    private PerOctetString() { /* 工具类，不可实例化 */ }

    // ==================== 固定长度 ====================

    /**
     * 编码固定长度的八位组串。
     *
     * @param pos 输出流
     * @param data 字节数据
     * @param fixedSize 固定字节长度
     * @throws IllegalArgumentException 如果 data 长度与 fixedSize 不符
     */
    public static void encodeFixedSize(PerOutputStream pos, byte[] data, int fixedSize) {
        if (fixedSize == 0) return;

        pos.align();

        int writeLen = Math.min(data != null ? data.length : 0, fixedSize);
        for (int i = 0; i < writeLen; i++) {
            pos.writeByteAligned(data[i]);
        }
        for (int i = writeLen; i < fixedSize; i++) {
            pos.writeByteAligned((byte) 0);
        }
    }

    /**
     * 解码固定长度的八位组串。
     *
     * @param pis 输入流
     * @param fixedSize 固定字节长度
     * @return 解码出的字节数组
     * @throws PerDecodeException 如果数据不足
     */
    public static byte[] decodeFixedSize(PerInputStream pis, int fixedSize) throws PerDecodeException {
        if (fixedSize == 0) return new byte[0];

        pis.align();
        return pis.readBytes(fixedSize);
    }

    // ==================== 变长（有约束范围） ====================

    /**
     * 编码变长八位组串。
     *
     * @param pos 输出流
     * @param data 字节数据
     * @param lowerBound 最小长度
     * @param upperBound 最大长度
     * @throws IllegalArgumentException 如果数据长度超出约束
     */
    public static void encodeConstrained(PerOutputStream pos, byte[] data,
                                         int lowerBound, int upperBound) {
        int actualLength = (data != null) ? data.length : 0;
        if (actualLength < lowerBound || actualLength > upperBound) {
            throw new IllegalArgumentException(
                String.format("OCTET STRING length %d out of range [%d, %d]",
                    actualLength, lowerBound, upperBound));
        }

        PerInteger.encode(pos, actualLength, lowerBound, upperBound);

        pos.align();
        for (int i = 0; i < actualLength; i++) {
            pos.writeByteAligned(data[i]);
        }
    }

    /**
     * 解码变长八位组串。
     *
     * @param pis 输入流
     * @param lowerBound 最小长度
     * @param upperBound 最大长度
     * @return 解码出的字节数组
     * @throws PerDecodeException 如果数据不足
     */
    public static byte[] decodeConstrained(PerInputStream pis,
                                           int lowerBound, int upperBound) throws PerDecodeException {
        int actualLength = (int) PerInteger.decode(pis, lowerBound, upperBound);
        if (actualLength == 0) return new byte[0];

        pis.align();
        return pis.readBytes(actualLength);
    }

    // ==================== 无约束 / 半约束 ====================

    /**
     * 编码半约束/无约束八位组串。
     *
     * @param pos 输出流
     * @param data 字节数据
     */
    public static void encodeUnconstrained(PerOutputStream pos, byte[] data) {
        int length = (data != null) ? data.length : 0;
        PerInteger.encodeLength(pos, length);
        if (length > 0) {
            pos.writeBytes(data.clone());
        }
    }

    /**
     * 解码半约束/无约束八位组串。
     *
     * @param pis 输入流
     * @return 解码出的字节数组
     * @throws PerDecodeException 如果数据不足
     */
    public static byte[] decodeUnconstrained(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) return new byte[0];
        return pis.readBytes(length);
    }
}
