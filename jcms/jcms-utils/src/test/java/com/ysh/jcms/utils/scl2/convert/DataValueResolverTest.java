package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.utils.scl2.SclDocument;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * DataValueResolver 单元测试。
 */
public class DataValueResolverTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl2.reader.SclReader reader = new com.ysh.jcms.utils.scl2.reader.SclReader();
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
        DataValue dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        assertNotNull("should find value", dv);
        assertEquals("false", dv.val());
        assertEquals("INT32", dv.bType());
    }

    @Test
    public void testResolveSdiBdaLevel() {
        SclDocument doc = parseFullScd();
        // MMXU1.Volts.sVC.offset → FLOAT32, value="10"
        DataValue dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.offset");
        assertNotNull("should find SDI value", dv);
        assertEquals("10", dv.val());
        assertEquals("FLOAT32", dv.bType());
    }

    @Test
    public void testResolveSdiBdaScaleFactor() {
        SclDocument doc = parseFullScd();
        DataValue dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.scaleFactor");
        assertNotNull("should find scaleFactor", dv);
        assertEquals("200", dv.val());
        assertEquals("FLOAT32", dv.bType());
    }

    @Test
    public void testResolveDoLevelNoFc() {
        SclDocument doc = parseFullScd();
        // DO level without FC: returns first DAI with a value
        DataValue dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy");
        assertNotNull("should find DO-level value", dv);
        // Proxy has one DAI (stVal), value="false"
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
        assertNull(DataValueResolver.resolve(null, "anything"));
    }
}
