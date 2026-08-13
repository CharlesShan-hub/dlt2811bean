package com.ysh.jcms.utils.scl.navigate;

import com.ysh.jcms.utils.scl.SclDocument;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Unit tests for Navigator / TypeChain / CmsDataTypeMap.
 */
public class NavigateTest {

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

    // ==================== Navigator ====================

    @Test
    public void testNavigatorLnLevel() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LLN0");
        assertTrue("ln-level should be valid", nav.isValid());
        assertNotNull(nav.ln());
        assertNull(nav.doi());
        assertEquals("LLN0", nav.ln().lnClass());
    }

    @Test
    public void testNavigatorDoLevel() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy");
        assertTrue("do-level should be valid", nav.isValid());
        assertNotNull("doi", nav.doi());
        assertNull("dai", nav.dai());
        assertEquals("Proxy", nav.doi().name());
    }

    @Test
    public void testNavigatorDaLevel() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        assertTrue("da-level should be valid", nav.isValid());
        assertNotNull(nav.doi());
        assertNull(nav.sdi());
        assertNotNull(nav.dai());
        assertEquals("Proxy", nav.doi().name());
        assertEquals("stVal", nav.dai().name());
        assertEquals("false", nav.daiValue());
    }

    @Test
    public void testNavigatorSdiChain() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.offset");
        assertTrue("sdi nav should be valid", nav.isValid());
        assertNotNull(nav.doi());
        assertNotNull(nav.sdi());
        assertNotNull(nav.dai());
        assertEquals("Volts", nav.doi().name());
        assertEquals("sVC", nav.sdi().name());
        assertEquals("offset", nav.dai().name());
        assertEquals("10", nav.daiValue());
    }

    @Test
    public void testNavigatorInvalidRef() {
        SclDocument doc = parseFullScd();
        assertFalse(Navigator.go(doc, "NONEXIST/LD/LN.DO.DA").isValid());
    }

    // ==================== TypeChain ====================

    @Test
    public void testTypeChainResolveBType() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        assertEquals("Enum", chain.resolveBType("LN0", "Mod.stVal"));
    }

    @Test
    public void testTypeChainStepByStep() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        assertEquals("Enum", chain.from("LN0").doDef("Mod").daDef("stVal").bType());
    }

    @Test
    public void testTypeChainCdc() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        assertEquals("INC", chain.from("LN0").doDef("Mod").cdc());
    }

    @Test
    public void testTypeChainStruct() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        assertEquals("Struct", chain.from("MMXUa").doDef("Amps").daDef("mag").bType());
        assertEquals("FLOAT32", chain.from("MMXUa").doDef("Amps").daDef("mag").daType().firstBdaBType());
    }

    @Test
    public void testTypeChainNullForMissing() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        assertNull(chain.from("NONEXIST").doDef("Mod").doType());
        assertNull(chain.from("LN0").doDef("NonExistent").doType());
    }

    @Test
    public void testTypeChainResolveSdiBda() {
        SclDocument doc = parseFullScd();
        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        String bType = chain.resolveBType("MMXUa", "Volts.sVC.scaleFactor");
        assertEquals("FLOAT32", bType);
    }

    // ==================== CmsDataTypeMap ====================

    @Test
    public void testCmsDataTypeMapSelector() {
        assertEquals(CmsDataTypeMap.SEL_BOOLEAN, CmsDataTypeMap.toSelector("BOOLEAN"));
        assertEquals(CmsDataTypeMap.SEL_INT32, CmsDataTypeMap.toSelector("INT32"));
        assertEquals(CmsDataTypeMap.SEL_FLOAT32, CmsDataTypeMap.toSelector("FLOAT32"));
        assertEquals(CmsDataTypeMap.SEL_QUALITY, CmsDataTypeMap.toSelector("Quality"));
        assertEquals(CmsDataTypeMap.SEL_UTC_TIME, CmsDataTypeMap.toSelector("Timestamp"));
        assertEquals(CmsDataTypeMap.SEL_VISIBLE_STRING, CmsDataTypeMap.toSelector("VisString255"));
        assertEquals(CmsDataTypeMap.SEL_STRUCTURE, CmsDataTypeMap.toSelector("Struct"));
    }

    @Test
    public void testCmsDataTypeMapNull() {
        assertEquals(CmsDataTypeMap.SEL_BOOLEAN, CmsDataTypeMap.toSelector(null));
        assertEquals(CmsDataTypeMap.SEL_BOOLEAN, CmsDataTypeMap.toSelector("UNKNOWN_TYPE"));
    }
}
