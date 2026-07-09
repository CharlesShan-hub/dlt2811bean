package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.navigate.CmsDataTypeMap;
import com.ysh.jcms.utils.scl2.navigate.Navigator;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class DataDefinitionResolverTest {

    private SclDocument parseFullScd() {
        try {
            com.ysh.jcms.utils.scl2.reader.SclReader reader = new com.ysh.jcms.utils.scl2.reader.SclReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample-scd-full.scd");
            return reader.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDaLevelInt32() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
        DataDefinitionEntry entry = DataDefinitionResolver.resolve(nav);
        assertNotNull(entry);
        // stVal bType=INT32 → choice=SEL_INT32=6
        assertEquals(6, entry.definition().choice.value());
    }

    @Test
    public void testDaLevelFloat32() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/MMXU1.Volts.sVC.offset");
        DataDefinitionEntry entry = DataDefinitionResolver.resolve(nav);
        assertNotNull(entry);
        // offset bType=FLOAT32 → choice=12
        assertEquals(CmsDataTypeMap.SEL_FLOAT32, entry.definition().choice.value());
    }

    @Test
    public void testDoLevelStructure() {
        SclDocument doc = parseFullScd();
        Navigator nav = Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy");
        DataDefinitionEntry entry = DataDefinitionResolver.resolve(nav);
        assertNotNull(entry);
        assertEquals(CmsDataTypeMap.SEL_STRUCTURE, entry.definition().choice.value());
        assertEquals("SPS", entry.cdcType());
        assertTrue(entry.definition().alt_structure.items.size() > 0);
    }

    @Test
    public void testToDataDefinitionBitString() {
        int choice = DataDefinitionResolver.toDataDefinition("BIT_STRING").choice.value();
        assertEquals(CmsDataTypeMap.SEL_BIT_STRING, choice);
    }

    @Test
    public void testToDataDefinitionVisibleString() {
        int choice = DataDefinitionResolver.toDataDefinition("VisString255").choice.value();
        assertEquals(CmsDataTypeMap.SEL_VISIBLE_STRING, choice);
    }
}
