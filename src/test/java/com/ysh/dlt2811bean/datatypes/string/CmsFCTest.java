package com.ysh.dlt2811bean.datatypes.string;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFC")
class CmsFCTest {

    @Test
    @DisplayName("default constructor defaults to ST")
    void defaultConstructor() {
        CmsFC fc = new CmsFC();
        assertEquals("ST", fc.get());
    }

    @Test
    @DisplayName("constructor with valid value")
    void constructorWithValidValue() {
        CmsFC fc = new CmsFC("MX");
        assertEquals("MX", fc.get());
    }

    @Test
    @DisplayName("constructor with null value throws")
    void constructorWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsFC(null));
    }

    @Test
    @DisplayName("constructor with invalid value throws")
    void constructorWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsFC("GG"));
    }

    @Test
    @DisplayName("constructor with wrong length throws")
    void constructorWithWrongLength() {
        assertThrows(IllegalArgumentException.class, () -> new CmsFC("ABC"));
    }

    @Test
    @DisplayName("set valid value")
    void setValidValue() {
        CmsFC fc = new CmsFC();
        fc.set("SV");
        assertEquals("SV", fc.get());
    }

    @Test
    @DisplayName("set invalid value throws")
    void setInvalidValue() {
        CmsFC fc = new CmsFC();
        assertThrows(IllegalArgumentException.class, () -> fc.set("GG"));
    }

    @Test
    @DisplayName("isValid returns true for valid values")
    void isValidValid() {
        assertTrue(CmsFC.isValid("ST"));
        assertTrue(CmsFC.isValid("MX"));
        assertTrue(CmsFC.isValid("SP"));
        assertTrue(CmsFC.isValid("SV"));
        assertTrue(CmsFC.isValid("CF"));
        assertTrue(CmsFC.isValid("DC"));
        assertTrue(CmsFC.isValid("SG"));
        assertTrue(CmsFC.isValid("SE"));
        assertTrue(CmsFC.isValid("SR"));
        assertTrue(CmsFC.isValid("OR"));
        assertTrue(CmsFC.isValid("BL"));
        assertTrue(CmsFC.isValid("EX"));
        assertTrue(CmsFC.isValid("XX"));
    }

    @Test
    @DisplayName("isValid returns false for invalid values")
    void isValidInvalid() {
        assertFalse(CmsFC.isValid("GG"));
        assertFalse(CmsFC.isValid("AA"));
        assertFalse(CmsFC.isValid(null));
        assertFalse(CmsFC.isValid(""));
        assertFalse(CmsFC.isValid("ABC"));
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        CmsFC fc = new CmsFC("MX");

        PerOutputStream pos = new PerOutputStream();
        fc.encode(pos);

        CmsFC decoded = new CmsFC().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("MX", decoded.get());
    }

    @Test
    @DisplayName("encode/decode default value")
    void encodeDecodeDefault() throws Exception {
        CmsFC fc = new CmsFC();

        PerOutputStream pos = new PerOutputStream();
        fc.encode(pos);

        CmsFC decoded = new CmsFC().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("ST", decoded.get());
    }

    @Test
    @DisplayName("encode/decode all valid values")
    void encodeDecodeAllValid() throws Exception {
        for (String valid : CmsFC.VALID_FC) {
            CmsFC fc = new CmsFC(valid);

            PerOutputStream pos = new PerOutputStream();
            fc.encode(pos);

            CmsFC decoded = new CmsFC().decode(new PerInputStream(pos.toByteArray()));
            assertEquals(valid, decoded.get());
        }
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsFC original = new CmsFC("MX");
        CmsFC cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsFC original = new CmsFC("MX");
        CmsFC cloned = original.copy();
        cloned.set("SV");
        assertEquals("MX", original.get());
        assertEquals("SV", cloned.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsFC fc = new CmsFC("ST");
        assertEquals("(CmsFC) ST", fc.toString());
    }
}
