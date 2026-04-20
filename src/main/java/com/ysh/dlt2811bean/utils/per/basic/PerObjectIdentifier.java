package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 OBJECT IDENTIFIER 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>OBJECT IDENTIFIER</b> (GB/T 16262 / X.680 §31)
 *
 * <p>DL/T 2811 标准典型引用:
 * <ul>
 *   <li>标准正文中未直接使用 OID 类型作为服务参数</li>
 *   <li>但 OID 常用于 MMS 协议层的域标识、类型标识等场景</li>
 *   <li>当与 61850 (IEC 61850) 互操作时可能需要</li>
 * </ul>
 *
 * <p>编码规则（X.691 §23）：
 * <ul>
 *   <li>OID 编码为一系列子标识符 (sub-identifier) 的字节序列</li>
 *   <li>前两个子标识符 (a, b) 合并为第一个字节: 40*a + b</li>
 *   <li>后续子标识符使用 BER 风格的可变长度编码（每 7 比特为一组，MSB 为续位标志）</li>
 *   <li>无约束格式：先按长度规则编码字节总数，再编码内容</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   // 编码 OID: 1.3.6.1 (iso.org.dod.internet)
 *   PerObjectIdentifier.encode(pos, new int[]{1, 3, 6, 1});
 *
 *   // 解码 OID
 *   int[] oid = PerObjectIdentifier.decode(pis);
 *   // oid = {1, 3, 6, 1}
 * </pre>
 */
public final class PerObjectIdentifier {

    private PerObjectIdentifier() { /* 工具类，不可实例化 */ }

    /**
     * 编码 OBJECT IDENTIFIER。
     *
     * @param pos 输出流
     * @param components OID 子标识符数组（如 {1, 3, 6, 1}）
     * @throws IllegalArgumentException 如果 components 无效
     */
    public static void encode(PerOutputStream pos, int[] components) {
        if (components == null || components.length == 0) {
            PerInteger.encodeLength(pos, 0);
            return;
        }

        // 编码 OID 字节内容
        byte[] content = encodeComponents(components);

        // 编码长度 + 内容
        PerInteger.encodeLength(pos, content.length);
        pos.writeBytes(content);
    }

    /**
     * 解码 OBJECT IDENTIFIER。
     *
     * @param pis 输入流
     * @return OID 子标识符数组
     * @throws PerDecodeException 如果数据不足或格式错误
     */
    public static int[] decode(PerInputStream pis) throws PerDecodeException {
        int length = PerInteger.decodeLength(pis);
        if (length == 0) {
            return new int[0];
        }

        byte[] content = pis.readBytes(length);
        return decodeComponents(content);
    }

    /**
     * 将 OID 数组转为点分十进制字符串表示。
     *
     * @param components OID 子标识符数组
     * @return 点分字符串，如 "1.3.6.1"
     */
    public static String toString(int[] components) {
        if (components == null || components.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            if (i > 0) sb.append('.');
            sb.append(components[i]);
        }
        return sb.toString();
    }

    /**
     * 从点分十进制字符串解析 OID 数组。
     *
     * @param oidStr 点分字符串，如 "1.3.6.1"
     * @return OID 子标识符数组
     * @throws IllegalArgumentException 如果格式无效
     */
    public static int[] fromString(String oidStr) {
        if (oidStr == null || oidStr.isEmpty()) {
            return new int[0];
        }
        String[] parts = oidStr.split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    // ==================== 内部方法 ====================

    /**
     * 将 OID 子标识符编码为 BER 风格字节序列。
     */
    private static byte[] encodeComponents(int[] components) {
        // 估算最大长度：每个子标识符最多 5 字节
        byte[] buf = new byte[components.length * 5 + 1];
        int pos = 0;

        if (components.length >= 1) {
            int a = components[0];
            int b = components.length >= 2 ? components[1] : 0;
            if (a > 2 || (a == 2 && b > 39)) {
                throw new IllegalArgumentException(
                    "Invalid OID root: first component must be 0, 1, or 2; got " + a);
            }
            buf[pos++] = (byte) (40 * a + b);
        }

        for (int i = 2; i < components.length; i++) {
            pos = encodeSubIdentifier(components[i], buf, pos);
        }

        byte[] result = new byte[pos];
        System.arraycopy(buf, 0, result, 0, pos);
        return result;
    }

    /**
     * 编码单个子标识符（BER 可变长度 7 比特编码）。
     */
    private static int encodeSubIdentifier(int value, byte[] buf, int pos) {
        if (value < 0x80) {
            buf[pos++] = (byte) value;
        } else if (value < 0x4000) {
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else if (value < 0x200000) {
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else if (value < 0x10000000) {
            buf[pos++] = (byte) (0x80 | ((value >> 21) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        } else {
            buf[pos++] = (byte) (0x80 | ((value >> 28) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 21) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 14) & 0x7F));
            buf[pos++] = (byte) (0x80 | ((value >> 7) & 0x7F));
            buf[pos++] = (byte) (value & 0x7F);
        }
        return pos;
    }

    /**
     * 从 BER 风格字节序列解码 OID 子标识符。
     */
    private static int[] decodeComponents(byte[] content) throws PerDecodeException {
        // 估算大小
        int[] estimate = new int[content.length + 1];
        int count = 0;

        if (content.length == 0) {
            return new int[0];
        }

        // 第一个字节是 40*a + b
        int first = content[0] & 0xFF;
        int a = first / 40;
        int b = first % 40;
        // a=2 时 b 可能 > 39，需要特殊处理
        if (a > 2) {
            b = first - 80;
            a = 2;
        }
        estimate[count++] = a;
        estimate[count++] = b;

        int bytePos = 1;
        while (bytePos < content.length) {
            long subId = 0;
            while (bytePos < content.length) {
                int b2 = content[bytePos++] & 0xFF;
                subId = (subId << 7) | (b2 & 0x7F);
                if ((b2 & 0x80) == 0) break;
            }
            if (subId > Integer.MAX_VALUE) {
                throw new PerDecodeException("OID sub-identifier too large: " + subId);
            }
            estimate[count++] = (int) subId;
        }

        int[] result = new int[count];
        System.arraycopy(estimate, 0, result, 0, count);
        return result;
    }
}
