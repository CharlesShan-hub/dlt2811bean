package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSubReference")
class CmsSubReferenceTest {

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsSubReference ref = new CmsSubReference();
        assertEquals("", ref.get());
    }

    @Test
    @DisplayName("constructor with valid value")
    void constructorWithValidValue() {
        CmsSubReference ref = new CmsSubReference("LN.DO.DA");
        assertEquals("LN.DO.DA", ref.get());
    }

    @Test
    @DisplayName("constructor with null value throws")
    void constructorWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsSubReference(null));
    }

    @Test
    @DisplayName("constructor with value too long throws")
    void constructorWithTooLongValue() {
        String tooLong = "A".repeat(CmsSubReference.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new CmsSubReference(tooLong));
    }

    @Test
    @DisplayName("constructor with slash throws")
    void constructorWithSlash() {
        assertThrows(IllegalArgumentException.class, () -> new CmsSubReference("LN/DO"));
    }

    @Test
    @DisplayName("set valid value")
    void setValidValue() {
        CmsSubReference ref = new CmsSubReference();
        ref.set("LN.DO.DA");
        assertEquals("LN.DO.DA", ref.get());
    }

    @Test
    @DisplayName("set with slash throws")
    void setWithSlash() {
        CmsSubReference ref = new CmsSubReference();
        assertThrows(IllegalArgumentException.class, () -> ref.set("LN/DO"));
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        CmsSubReference ref = new CmsSubReference("LN.DO.DA");

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsSubReference decoded = new CmsSubReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("LN.DO.DA", decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty string")
    void encodeDecodeEmpty() throws Exception {
        CmsSubReference ref = new CmsSubReference();

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsSubReference decoded = new CmsSubReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", decoded.get());
    }

    @Test
    @DisplayName("encode/decode max length string")
    void encodeDecodeMaxLength() throws Exception {
        String maxLen = "A".repeat(CmsSubReference.MAX_LENGTH);
        CmsSubReference ref = new CmsSubReference(maxLen);

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsSubReference decoded = new CmsSubReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(maxLen, decoded.get());
    }

    @Test
    @DisplayName("validate static method")
    void validateStatic() {
        assertDoesNotThrow(() -> CmsSubReference.validate("LN.DO.DA"));
        assertDoesNotThrow(() -> CmsSubReference.validate(""));
        assertDoesNotThrow(() -> CmsSubReference.validate(null));

        assertThrows(IllegalArgumentException.class, () -> CmsSubReference.validate("LN/DO"));
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsSubReference original = new CmsSubReference("LN.DO.DA");
        CmsSubReference cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsSubReference original = new CmsSubReference("LN.DO.DA");
        CmsSubReference cloned = original.copy();
        cloned.set("LN2.DO2.DA2");
        assertEquals("LN.DO.DA", original.get());
        assertEquals("LN2.DO2.DA2", cloned.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsSubReference ref = new CmsSubReference("LN.DO.DA");
        assertEquals("SubReference: LN.DO.DA", ref.toString());
    }
}
