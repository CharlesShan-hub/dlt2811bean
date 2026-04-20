package com.ysh.dlt2811bean.utils.per.basic;

import com.ysh.dlt2811bean.utils.per.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.PerInputStream;
import com.ysh.dlt2811bean.utils.per.PerOutputStream;

/**
 * ASN.1 NULL 类型的 APER 编解码。
 *
 * <p>ASN.1 类型: <b>NULL</b> (GB/T 16262 / X.680 §18)
 *
 * <p>DL/T 2811 标准典型引用: 空响应体（如 DeleteFile Response+）
 *
 * <p>编码规则（X.691 §13）：
 * <ul>
 *   <li>编码长度：0 比特（不编码任何内容）</li>
 *   <li>类型本身已携带全部信息</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>
 *   PerNull.encode(pos);    // 不写入任何数据
 *   PerNull.decode(pis);    // 不读取任何数据
 * </pre>
 */
public final class PerNull {

    private PerNull() { /* 工具类，不可实例化 */ }

    /**
     * 编码 NULL 值（不写入任何数据）。
     *
     * @param pos 输出流
     */
    public static void encode(PerOutputStream pos) {
        // 不编码任何内容
    }

    /**
     * 解码 NULL 值（不读取任何数据）。
     *
     * @param pis 输入流
     */
    public static void decode(PerInputStream pis) {
        // 不读取任何内容
    }
}
