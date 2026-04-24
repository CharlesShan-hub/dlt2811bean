package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectReference")
class CmsObjectReferenceTest {

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsObjectReference ref = new CmsObjectReference();
        assertEquals("", ref.get());
    }

    @Test
    @DisplayName("constructor with valid value")
    void constructorWithValidValue() {
        CmsObjectReference ref = new CmsObjectReference("LD1/LN1.DO1");
        assertEquals("LD1/LN1.DO1", ref.get());
    }

    @Test
    @DisplayName("constructor with null value throws")
    void constructorWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectReference(null));
    }

    @Test
    @DisplayName("constructor with value too long throws")
    void constructorWithTooLongValue() {
        String tooLong = "A".repeat(CmsObjectReference.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectReference(tooLong));
    }

    @Test
    @DisplayName("constructor with dollar sign throws")
    void constructorWithDollarSign() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectReference("LD1/LN1$DO1"));
    }

    @Test
    @DisplayName("constructor with FC suffix throws")
    void constructorWithFCSuffix() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectReference("LD1/LN1.DO1.ST"));
    }

    @Test
    @DisplayName("constructor with lowercase FC suffix throws")
    void constructorWithLowercaseFCSuffix() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectReference("LD1/LN1.DO1.st"));
    }

    @Test
    @DisplayName("set valid value")
    void setValidValue() {
        CmsObjectReference ref = new CmsObjectReference();
        ref.set("LD1/LN1.DO1");
        assertEquals("LD1/LN1.DO1", ref.get());
    }

    @Test
    @DisplayName("set with dollar sign throws")
    void setWithDollarSign() {
        CmsObjectReference ref = new CmsObjectReference();
        assertThrows(IllegalArgumentException.class, () -> ref.set("LD1/LN1$DO1"));
    }

    @Test
    @DisplayName("set with FC suffix throws")
    void setWithFCSuffix() {
        CmsObjectReference ref = new CmsObjectReference();
        assertThrows(IllegalArgumentException.class, () -> ref.set("LD1/LN1.DO1.MX"));
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        CmsObjectReference ref = new CmsObjectReference("LD1/LN1.DO1");

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsObjectReference decoded = new CmsObjectReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("LD1/LN1.DO1", decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty string")
    void encodeDecodeEmpty() throws Exception {
        CmsObjectReference ref = new CmsObjectReference();

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsObjectReference decoded = new CmsObjectReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", decoded.get());
    }

    @Test
    @DisplayName("encode/decode max length string")
    void encodeDecodeMaxLength() throws Exception {
        String maxLen = "A".repeat(CmsObjectReference.MAX_LENGTH);
        CmsObjectReference ref = new CmsObjectReference(maxLen);

        PerOutputStream pos = new PerOutputStream();
        ref.encode(pos);

        CmsObjectReference decoded = new CmsObjectReference().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(maxLen, decoded.get());
    }

    @Test
    @DisplayName("validate static method")
    void validateStatic() {
        assertDoesNotThrow(() -> CmsObjectReference.validate("LD1/LN1.DO1"));
        assertDoesNotThrow(() -> CmsObjectReference.validate(""));
        assertDoesNotThrow(() -> CmsObjectReference.validate(null));

        assertThrows(IllegalArgumentException.class, () -> CmsObjectReference.validate("LD1/LN1$DO1"));
        assertThrows(IllegalArgumentException.class, () -> CmsObjectReference.validate("LD1/LN1.DO1.ST"));
        assertThrows(IllegalArgumentException.class, () -> CmsObjectReference.validate("LD1/LN1.DO1.st"));
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsObjectReference ref = new CmsObjectReference("LD1/LN1.DO1");
        assertEquals("ObjectReference: LD1/LN1.DO1", ref.toString());
    }
}
