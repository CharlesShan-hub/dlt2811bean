package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.SclDocument;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * DataValueResolver 单元测试。
 */
public class DataValueResolverTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl.reader.SclReader reader = new com.ysh.jcms.utils.scl.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            assertNotNull("sample-scd-full.scd not found on classpath", is);
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testResolveDaLevel() {
        SclDocument doc = parseFullScd();
        // LPHD1.Proxy.stVal → INT32, value="false"
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        assertNotNull("should find value", dv);
        assertEquals("false", dv.val());
        assertEquals("INT32", dv.bType());
    }

    @Test
    public void testResolveSdiBdaLevel() {
        SclDocument doc = parseFullScd();
        // MMXU1.Volts.sVC.offset → FLOAT32, value="10"
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.offset");
        assertNotNull("should find SDI value", dv);
        assertEquals("10", dv.val());
        assertEquals("FLOAT32", dv.bType());
    }

    @Test
    public void testResolveSdiBdaScaleFactor() {
        SclDocument doc = parseFullScd();
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.scaleFactor");
        assertNotNull("should find scaleFactor", dv);
        assertEquals("200", dv.val());
        assertEquals("FLOAT32", dv.bType());
    }

    @Test
    public void testResolveDoLevelNoFc() {
        SclDocument doc = parseFullScd();
        // DO level without FC: returns first DAI with a value
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy");
        assertNotNull("should find DO-level value", dv);
        // Proxy has one DAI (stVal), value="false"
        assertEquals("false", dv.val());
    }

    @Test
    public void testResolveTemplateDefaultWhenNoDai() {
        SclDocument doc = parseFullScd();
        // LLN0 的 NamPlt 无 DOI 实例，值写在 DOType 模板的 DA/<Val> 里 → 实例无值时应兜底模板默认值
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LLN0.NamPlt.configRev");
        assertNotNull("should fall back to template default", dv);
        assertEquals("Rev 3.45", dv.val());

        DataValueEntry ldNs = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LLN0.NamPlt.ldNs");
        assertNotNull("should fall back to template default", ldNs);
        assertEquals("IEC61850-7-4:2003", ldNs.val());
    }

    @Test
    public void testInstanceValueWinsOverTemplate() {
        SclDocument doc = parseFullScd();
        // 实例 DAI 有值时优先，模板默认值不覆盖
        DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        assertEquals("false", dv.val());
    }

    @Test
    public void testResolveInvalidRef() {
        SclDocument doc = parseFullScd();
        assertNull(DataValueResolver.resolve(doc, "NONEXIST/LD/LN.DO.DA"));
        assertNull(DataValueResolver.resolve(doc, "E1Q1SB1/C1/LLN0")); // LN level, no value
    }

    @Test
    public void testResolveNullDoc() {
        assertNull(DataValueResolver.resolve((SclDocument) null, "anything"));
    }
}
