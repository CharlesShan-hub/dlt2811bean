package com.ysh.jcms.core.data.sequence;

import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.sequence.common.CmsFileEntry;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 验证设计共识：
 * 
 * <pre>
 * 1. Setter/Getter 原地操作 — 直接读写 _v 树，不创建新对象
 * 2. decode 生成新树 — 替换 Inner*，通过 rebind 重建 _v 引用
 * 
 * 测试场景（嵌套两层）：
 * 
 *   CmsFileEntry (CmsSequence)                    ← A 和 C 是同一个类
 *     ├── fileName: CmsString
 *     ├── fileSize: CmsInt32U
 *     ├── lastModified: CmsUtcTime (CmsSequence)  ← 嵌套序列
 *     │   ├── secondsSinceEpoch: CmsInt32U
 *     │   ├── fractionOfSecond: CmsInt24U
 *     │   └── timeQuality: CmsTimeQuality (CmsBits)
 *     └── checkSum: CmsInt32U
 * 
 *   A.lastModified.secondsSinceEpoch = 1234567890
 *   C.lastModified.secondsSinceEpoch = 4000000000
 *   
 *   A.encode → bytes → C.decode(bytes)
 *   
 *   验证：
 *   - c.lastModified 对象引用前后不变（wrapper 原地复用）
 *   - c.lastModified 的值来自 A 的新树
 *   - B（独立构建的 CmsUtcTime）的值不受影响
 * </pre>
 */
public class CmsDecodeReplaceTreeTest {

    @Test
    public void decode替换树后嵌套字段值来自新树() {
        // ── B：独立构建的 CmsUtcTime ──
        CmsUtcTime b = new CmsUtcTime()
                .secondsSinceEpoch(1234567890L)
                .fractionOfSecond(500000)
                .timeQuality(new CmsTimeQuality()
                        .leap_seconds_known(true)
                        .clock_failure(false)
                        .clock_not_synchronized(false)
                        .precision(24));

        // ── A：含有 B（值拷贝到 A 的 _v 树） ──
        CmsFileEntry a = new CmsFileEntry()
                .fileName("a.txt")
                .fileSize(1024L)
                .lastModified(b)
                .checkSum(0xDEADBEEFL);

        // ── C：含有自己的 CmsUtcTime（也是值拷贝） ──
        CmsFileEntry c = new CmsFileEntry()
                .fileName("c.txt")
                .fileSize(2048L)
                .lastModified(new CmsUtcTime()
                        .secondsSinceEpoch(4000000000L)
                        .fractionOfSecond(100000)
                        .timeQuality(new CmsTimeQuality()
                                .leap_seconds_known(false)
                                .clock_failure(true)
                                .clock_not_synchronized(true)
                                .precision(10)))
                .checkSum(0xCAFEBABEL);

        // 记住 C 的 lastModified 对象引用
        CmsUtcTime cTmRef = c.lastModified;

        // 验证 C 原来的值
        assertEquals("C decode前 lastModified.secondsSinceEpoch = 4000000000",
                4000000000L, c.lastModified.secondsSinceEpoch.value());

        // ── A.encode → bytes → C.decode ──
        byte[] encoded = a.encode();
        c.decode(encoded);

        // ── 验证 ──
        // 1. C.lastModified 对象引用不变（wrapper 原地复用）
        assertSame("decode后 wrapper 对象引用不变", cTmRef, c.lastModified);

        // 2. C.lastModified 的 inner._v 已指向新树，值来自 A
        assertEquals("C.lastModified.secondsSinceEpoch 来自 A",
                1234567890L, c.lastModified.secondsSinceEpoch.value());

        // 3. 嵌套的第三层（timeQuality）也同步更新
        assertEquals("C.lastModified.timeQuality.leap_seconds_known 来自 A",
                true, c.lastModified.timeQuality.leap_seconds_known());
        assertEquals("C.lastModified.timeQuality.clock_failure 来自 A",
                false, c.lastModified.timeQuality.clock_failure());

        // 4. 顶层字段也同步更新
        assertEquals("C.fileName 来自 A", "a.txt", c.fileName.value());
        assertEquals("C.fileSize 来自 A", 1024L, c.fileSize.value());
        assertEquals("C.checkSum 来自 A", 0xDEADBEEFL, c.checkSum.value());

        // 5. B 是独立构建的，其值不受 decode 影响
        assertEquals("B 的值不变", 1234567890L, b.secondsSinceEpoch.value());

        // 6. 完整相等
        assertEquals("C decode后应该等于A", a, c);
    }
}