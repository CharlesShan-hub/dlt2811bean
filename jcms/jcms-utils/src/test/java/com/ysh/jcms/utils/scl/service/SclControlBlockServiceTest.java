package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.sequence.block.CmsUrcb;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.reader.SclReader;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * 控制块服务测试 —— ref 解析 + SCL 默认值 + 运行时 overlay（用 sample-scd-full.scd 的 E1Q1SB1 IED）。
 */
public class SclControlBlockServiceTest {

    private SclDocument doc;
    private SclIED ied;
    private SclAccessPoint ap;

    @Before
    public void setUp() {
        try {
            SclReader reader = new SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            assertNotNull("sample-scd-full.scd not found on classpath", is);
            doc = reader.read(is);
            ied = doc.ied("E1Q1SB1");
            assertNotNull("IED E1Q1SB1 not found", ied);
            ap = ied.accessPoints().get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 清空静态运行时状态，避免测试间污染（resolveGocb/msvcb 优先读缓存）
        CbStateManager.clearAll();
    }

    // ==================== URCB（无 buffered 属性归 URCB） ====================

    @Test
    public void testResolveUrcbFromSclDefaults() {
        CmsUrcb urcb = SclControlBlockService.resolveUrcb(ied, ap, "C1/LLN0.PosReport");
        assertNotNull(urcb);
        assertEquals("RC", urcb.rptID.value());
        assertEquals("Positions", urcb.datSet.value());
        assertEquals(1L, urcb.confRev.value());
        assertFalse(urcb.rptEna.value());
        assertEquals(0L, urcb.sqNum.value());
    }

    @Test
    public void testResolveUrcbIntgPd() {
        CmsUrcb urcb = SclControlBlockService.resolveUrcb(ied, ap, "C1/LLN0.MeaReport");
        assertNotNull(urcb);
        assertEquals("Measurands", urcb.datSet.value());
        assertEquals(2000L, urcb.intgPd.value());
    }

    @Test
    public void testResolveBrcbReturnsNullWhenNoBuffered() {
        // sample 的 ReportControl 均无 buffered 属性 → BRCB 无匹配
        assertNull(SclControlBlockService.resolveBrcb(ied, ap, "C1/LLN0.PosReport"));
    }

    @Test
    public void testResolveUrcbUnknownRef() {
        assertNull(SclControlBlockService.resolveUrcb(ied, ap, "C1/LLN0.NotExist"));
        assertNull(SclControlBlockService.resolveUrcb(ied, ap, "bad-ref"));
    }

    // ==================== GoCB（8.10.2） ====================

    @Test
    public void testResolveGocbFromSclDefaults() {
        CmsGoCb gocb = SclControlBlockService.resolveGocb(ied, ap, "C1/LLN0.ItlPositions");
        assertNotNull(gocb);
        assertEquals("Itl", gocb.goID.value());
        assertEquals("Positions", gocb.datSet.value());
        assertEquals(1L, gocb.confRev.value());
    }

    @Test
    public void testResolveGocbPrefersRuntimeCache() {
        // 先写运行时状态，resolve 应优先返回缓存而非 SCL 值
        CmsGoCb rt = new CmsGoCb();
        rt.goID("RuntimeGoID");
        rt.datSet("RuntimeDs");
        CbStateManager.GOCB.put("C1/LLN0.ItlPositions", rt);

        CmsGoCb gocb = SclControlBlockService.resolveGocb(ied, ap, "C1/LLN0.ItlPositions");
        assertNotNull(gocb);
        assertEquals("RuntimeGoID", gocb.goID.value());
        assertEquals("RuntimeDs", gocb.datSet.value());
    }

    @Test
    public void testResolveGocbUnknownRef() {
        assertNull(SclControlBlockService.resolveGocb(ied, ap, "C1/LLN0.NoSuchCb"));
    }

    // ==================== MSVCB（8.11.2） ====================

    @Test
    public void testResolveMsvcbFromSclDefaults() {
        CmsMsvcb msvcb = SclControlBlockService.resolveMsvcb(ied, ap, "C1/LLN0.Volt");
        assertNotNull(msvcb);
        assertEquals("11", msvcb.msvID.value());
        assertEquals("smv", msvcb.datSet.value());
        assertEquals(1L, msvcb.confRev.value());
    }

    @Test
    public void testResolveMsvcbUnknownRef() {
        assertNull(SclControlBlockService.resolveMsvcb(ied, ap, "C1/LLN0.NoSuchCb"));
    }

    // ==================== LCB（8.8.2） ====================

    @Test
    public void testResolveLcb() {
        CmsLcb lcb = SclControlBlockService.resolveLcb(ied, ap, "C1/LLN0.Log");
        assertNotNull(lcb);
        assertEquals("Positions", lcb.datSet.value());
        assertEquals("C1", lcb.logRef.value());
    }

    @Test
    public void testResolveLcbUnknownRef() {
        assertNull(SclControlBlockService.resolveLcb(ied, ap, "C1/LLN0.NoSuchCb"));
    }
}
