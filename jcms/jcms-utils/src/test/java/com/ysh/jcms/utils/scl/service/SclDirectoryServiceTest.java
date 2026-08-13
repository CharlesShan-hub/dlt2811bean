package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.reader.SclReader;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 目录服务测试 —— 8.3.1/8.3.2/8.3.3（用 sample-scd-full.scd 的 E1Q1SB1 IED）。
 */
public class SclDirectoryServiceTest {

    private SclDocument doc;
    private SclIED ied;
    private SclAccessPoint ap;
    private SclLDevice ld;
    private List<SclLN> lns;

    private void load() {
        try {
            SclReader reader = new SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            assertNotNull("sample-scd-full.scd not found on classpath", is);
            doc = reader.read(is);
            ied = doc.ied("E1Q1SB1");
            assertNotNull("IED E1Q1SB1 not found", ied);
            ap = ied.accessPoints().get(0);
            ld = ap.server().lDevices().get(0);
            lns = ld.lns();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 服务器目录（8.3.1） ====================

    @Test
    public void testGetServerDirectory() {
        load();
        assertEquals(java.util.Collections.singletonList("C1"), SclDirectoryService.getServerDirectory(ap));
    }

    // ==================== 逻辑设备目录（8.3.2） ====================

    @Test
    public void testGetLogicalDeviceDirectoryWithLdName() {
        load();
        List<String> names = SclDirectoryService.getLogicalDeviceDirectory(ap, "C1");
        assertNotNull(names);
        // LLN0 移到首位，其余保持解析顺序（LN 完整名 = lnClass + inst）
        assertEquals("LLN0", names.get(0));
        assertEquals(6, names.size());
        assertTrue(names.contains("LPHD1"));
        assertTrue(names.contains("CSWI1"));
        assertTrue(names.contains("MMXU1"));
    }

    @Test
    public void testGetLogicalDeviceDirectoryAllLds() {
        load();
        // ldName 为空 → 全站 "LD/LN" 完整引用
        List<String> names = SclDirectoryService.getLogicalDeviceDirectory(ap, null);
        assertNotNull(names);
        assertTrue(names.contains("C1/LLN0"));
        assertTrue(names.contains("C1/MMXU1"));
    }

    @Test
    public void testGetLogicalDeviceDirectoryUnknownLd() {
        load();
        assertNull(SclDirectoryService.getLogicalDeviceDirectory(ap, "NOPE"));
    }

    // ==================== 逻辑节点目录（8.3.3） ====================

    @Test
    public void testGetLogicalNodeDirectoryDataSet() {
        load();
        List<String> names = SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.DATA_SET);
        assertEquals(java.util.Arrays.asList("Positions", "Measurands", "smv"), names);
    }

    @Test
    public void testGetLogicalNodeDirectoryReportControls() {
        load();
        // sample 的 ReportControl 无 buffered 属性 → 全部归 URCB
        List<String> urcb = SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.URCB);
        assertEquals(java.util.Arrays.asList("PosReport", "MeaReport"), urcb);
        List<String> brcb = SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.BRCB);
        assertTrue(brcb.isEmpty());
    }

    @Test
    public void testGetLogicalNodeDirectoryGooseAndMsv() {
        load();
        assertEquals(java.util.Collections.singletonList("ItlPositions"),
                SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.GOCB));
        assertEquals(java.util.Collections.singletonList("Volt"),
                SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.MSVCB));
    }

    @Test
    public void testGetLogicalNodeDirectoryLog() {
        load();
        // LCB：LogControl 名称；LOG：logName（C1）
        assertEquals(java.util.Collections.singletonList("Log"),
                SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.LCB));
        assertEquals(java.util.Collections.singletonList("C1"),
                SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.LOG));
    }

    @Test
    public void testGetLogicalNodeDirectoryDataObject() {
        load();
        List<String> names = SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.DATA_OBJECT);
        // LN0 类型有 Mod/Health/Beh/NamPlt 4 个 DO，引用前缀为 "C1/LLN0."
        assertTrue(names.contains("C1/LLN0.Mod"));
        assertTrue(names.contains("C1/LLN0.Health"));
        assertTrue(names.contains("C1/LLN0.Beh"));
    }

    @Test
    public void testGetLogicalNodeDirectoryUnknownClass() {
        load();
        assertTrue(SclDirectoryService.getLogicalNodeDirectory(doc, lns, "C1", CmsAcsiClass.SGCB).isEmpty());
    }
}
