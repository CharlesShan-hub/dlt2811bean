package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class DataSetResolverTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl.reader.SclReader reader = new com.ysh.jcms.utils.scl.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFcdaRef() {
        SclFCDA fcda = new SclFCDA().ldInst("C1").lnClass("MMXU").lnInst("1").doName("Volts").daName("mag");
        assertEquals("C1/MMXU1.Volts.mag", DataSetResolver.fcdaRef(fcda));
    }

    @Test
    public void testFcdaRefDoLevel() {
        SclFCDA fcda = new SclFCDA().ldInst("C1").lnClass("CSWI").lnInst("1").doName("Pos");
        assertEquals("C1/CSWI1.Pos", DataSetResolver.fcdaRef(fcda));
    }

    @Test
    public void testFcdaLnName() {
        SclFCDA fcda = new SclFCDA().lnClass("MMXU").lnInst("1");
        assertEquals("MMXU1", DataSetResolver.fcdaLnName(fcda));
    }

    @Test
    public void testParseRef() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        SclFCDA fcda = DataSetResolver.parseRef(nav);
        assertNotNull(fcda);
        assertEquals("C1", fcda.ldInst());
        assertEquals("LPHD", fcda.lnClass());
        assertEquals("1", fcda.lnInst());
        assertEquals("Proxy", fcda.doName());
        assertEquals("stVal", fcda.daName());
    }

    @Test
    public void testParseRefInvalid() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "NONEXIST/LD/LN");
        assertNull(DataSetResolver.parseRef(nav));
    }
}
