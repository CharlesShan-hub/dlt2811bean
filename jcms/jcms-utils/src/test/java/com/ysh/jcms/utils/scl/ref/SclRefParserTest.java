package com.ysh.jcms.utils.scl.ref;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SclRefParser 单元测试。
 */
public class SclRefParserTest {

    // ==================== 有效格式 ====================

    @Test
    public void testParseLnLevel() {
            SclRef ref = SclRefParser.parse("LD1/LLN0");
        assertEquals("LD1", ref.ldName());
        assertEquals("LLN0", ref.lnName());
        assertNull(ref.doName());
        assertNull(ref.daName());
        assertNull(ref.fc());
        assertTrue(ref.isLnLevel());
        assertFalse(ref.isDoLevel());
        assertFalse(ref.isDaLevel());
    }

    @Test
    public void testParseDoLevel() {
            SclRef ref = SclRefParser.parse("LD1/LLN0.Mod");
        assertEquals("LD1", ref.ldName());
        assertEquals("LLN0", ref.lnName());
        assertEquals("Mod", ref.doName());
        assertNull(ref.daName());
        assertNull(ref.fc());
        assertFalse(ref.isLnLevel());
        assertTrue(ref.isDoLevel());
        assertFalse(ref.isDaLevel());
    }

    @Test
    public void testParseDaLevel() {
            SclRef ref = SclRefParser.parse("LD1/LLN0.Mod.stVal");
        assertEquals("LD1", ref.ldName());
        assertEquals("LLN0", ref.lnName());
        assertEquals("Mod", ref.doName());
        assertEquals("stVal", ref.daName());
        assertNull(ref.fc());
        assertFalse(ref.isLnLevel());
        assertFalse(ref.isDoLevel());
        assertTrue(ref.isDaLevel());
    }

    @Test
    public void testParseDaLevelWithFc() {
            SclRef ref = SclRefParser.parse("C1/MMXU1.Amps.mag[MX]");
        assertEquals("C1", ref.ldName());
        assertEquals("MMXU1", ref.lnName());
        assertEquals("Amps", ref.doName());
        assertEquals("mag", ref.daName());
        assertEquals("MX", ref.fc());
        assertTrue(ref.hasFc());
    }

    @Test
    public void testParseComplexNames() {
            SclRef ref = SclRefParser.parse("LD_123/ABC_Def.SomeDO.someDA[ST]");
        assertEquals("LD_123", ref.ldName());
        assertEquals("ABC_Def", ref.lnName());
        assertEquals("SomeDO", ref.doName());
        assertEquals("someDA", ref.daName());
        assertEquals("ST", ref.fc());
    }

    // ==================== 无效格式 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testParseNull() {
        SclRefParser.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmpty() {
        SclRefParser.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBlank() {
        SclRefParser.parse("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingLd() {
        SclRefParser.parse("/LLN0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingLn() {
        SclRefParser.parse("LD1/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseGarbage() {
        SclRefParser.parse("not-a-reference!!!");
    }

    // ==================== isValid ====================

    @Test
    public void testIsValid() {
        assertTrue(SclRefParser.isValid("LD1/LLN0"));
        assertTrue(SclRefParser.isValid("LD1/LLN0.Mod"));
        assertTrue(SclRefParser.isValid("LD1/LLN0.Mod.stVal"));
        assertTrue(SclRefParser.isValid("LD1/LLN0.Mod.stVal[ST]"));
        assertFalse(SclRefParser.isValid(null));
        assertFalse(SclRefParser.isValid(""));
        assertFalse(SclRefParser.isValid("   "));
        assertFalse(SclRefParser.isValid("garbage"));
    }

    // ==================== extract ====================

    @Test
    public void testExtractLnReference() {
        assertEquals("LD1/LLN0", SclRefParser.extractLnReference("LD1/LLN0.Mod.stVal"));
        assertEquals("LD1/LLN0", SclRefParser.extractLnReference("LD1/LLN0"));
    }

    @Test
    public void testExtractDoReference() {
        assertEquals("LD1/LLN0.Mod", SclRefParser.extractDoReference("LD1/LLN0.Mod.stVal[MX]"));
    }

    // ==================== 工厂方法 ====================

    @Test
    public void testSclRefOf() {
        SclRef ref = SclRef.ld("LD1").lnName("LLN0").doName("Mod").daName("stVal").build();
        assertEquals("LD1/LLN0.Mod.stVal", ref.fullReference());
        assertTrue(ref.isDaLevel());
    }

    @Test
    public void testSclRefOfDoLevel() {
        SclRef ref = SclRef.ld("LD1").lnName("LLN0").doName("Mod").build();
        assertEquals("LD1/LLN0.Mod", ref.fullReference());
        assertTrue(ref.isDoLevel());
    }

    @Test
    public void testSclRefOfLnLevel() {
        SclRef ref = SclRef.ld("LD1").lnName("LLN0").build();
        assertEquals("LD1/LLN0", ref.fullReference());
        assertTrue(ref.isLnLevel());
    }

    // ==================== equals/hashCode ====================

    @Test
    public void testEquals() {
        SclRef a = SclRefParser.parse("LD1/LLN0.Mod.stVal");
        SclRef b = SclRefParser.parse("LD1/LLN0.Mod.stVal");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEquals() {
        SclRef a = SclRefParser.parse("LD1/LLN0.Mod.stVal");
        SclRef b = SclRefParser.parse("LD1/LLN0.Mod");
        assertNotEquals(a, b);
    }

    // ==================== getLnReference / getDoReference ====================

    @Test
    public void testGetLnReference() {
        SclRef ref = SclRefParser.parse("C1/MMXU1.Amps.mag[MX]");
        assertEquals("C1/MMXU1", ref.lnReference());
    }

    @Test
    public void testGetDoReference() {
        SclRef ref = SclRefParser.parse("C1/MMXU1.Amps.mag[MX]");
        assertEquals("C1/MMXU1.Amps", ref.doReference());
    }

    @Test
    public void testGetDaReference() {
        SclRef ref = SclRefParser.parse("C1/MMXU1.Amps.mag[MX]");
        assertEquals("C1/MMXU1.Amps.mag", ref.daReference());
    }
}
