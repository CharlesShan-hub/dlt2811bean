package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 BOOLEAN 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>BOOLEAN</b> (GB/T 16262 / X.680 §16)
 *
 * <p>DL/T 2811 标准典型引用: moreFollows[0..1] BOOLEAN (各类服务的分页标志)
 *
 * <p>编码规则（X.691 §12）：
 * <ul>
 *   <li>编码长度：恰好 1 比特</li>
 *   <li>FALSE → 0, TRUE → 1</li>
 *   <li>无对齐要求</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   PerBoolean.encode(pos, true);    // 写 1 比特: 1
 *   boolean b = PerBoolean.decode(pis); // 读 1 比特
 * </pre>
 */
public final class PerBoolean {

    private PerBoolean() { /* 工具类，不可实例化 */ }

    /**
     * 编码布尔值。
     *
     * @param pos   输出流
     * @param value 布尔值
     */
    public static void encode(PerOutputStream pos, boolean value) {
        pos.writeBit(value);
    }

    /**
     * 解码布尔值。
     *
     * @param pis 输入流
     * @return 解码出的布尔值
     * @throws PerDecodeException 如果数据不足
     */
    public static boolean decode(PerInputStream pis) throws PerDecodeException {
        return pis.readBit();
    }
}
