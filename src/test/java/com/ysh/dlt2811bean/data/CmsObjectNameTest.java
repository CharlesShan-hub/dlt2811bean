package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.string.CmsObjectName;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsObjectName")
class CmsObjectNameTest {

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsObjectName name = new CmsObjectName();
        assertEquals("", name.get());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsObjectName name = new CmsObjectName("LD1");
        assertEquals("LD1", name.get());
    }

    @Test
    @DisplayName("constructor with null value throws")
    void constructorWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectName(null));
    }

    @Test
    @DisplayName("constructor with max length value")
    void constructorWithMaxLengthValue() {
        String maxLen = "A".repeat(CmsObjectName.MAX_LENGTH);
        CmsObjectName name = new CmsObjectName(maxLen);
        assertEquals(maxLen, name.get());
    }

    @Test
    @DisplayName("constructor throws on value too long")
    void constructorThrowsOnTooLong() {
        String tooLong = "A".repeat(CmsObjectName.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectName(tooLong));
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsObjectName name = new CmsObjectName();
        name.set("LD2");
        assertEquals("LD2", name.get());
    }

    @Test
    @DisplayName("set throws on null")
    void setThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectName().set(null));
    }

    @Test
    @DisplayName("set throws on value too long")
    void setThrowsOnTooLong() {
        String tooLong = "A".repeat(CmsObjectName.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new CmsObjectName().set(tooLong));
    }

    @Test
    @DisplayName("set accepts max length value")
    void setAcceptsMaxLength() {
        String maxLen = "B".repeat(CmsObjectName.MAX_LENGTH);
        CmsObjectName name = new CmsObjectName().set(maxLen);
        assertEquals(maxLen, name.get());
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        CmsObjectName name = new CmsObjectName("LD1");

        PerOutputStream pos = new PerOutputStream();
        name.encode(pos);

        CmsObjectName decoded = new CmsObjectName().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("LD1", decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty string")
    void encodeDecodeEmpty() throws Exception {
        CmsObjectName name = new CmsObjectName();

        PerOutputStream pos = new PerOutputStream();
        name.encode(pos);

        CmsObjectName decoded = new CmsObjectName().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", decoded.get());
    }

    @Test
    @DisplayName("encode/decode max length string")
    void encodeDecodeMaxLength() throws Exception {
        String maxLen = "C".repeat(CmsObjectName.MAX_LENGTH);
        CmsObjectName name = new CmsObjectName(maxLen);

        PerOutputStream pos = new PerOutputStream();
        name.encode(pos);

        CmsObjectName decoded = new CmsObjectName().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(maxLen, decoded.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsObjectName name = new CmsObjectName().set("LD1");
        assertEquals("LD1", name.get());

        PerOutputStream pos = new PerOutputStream();
        name.encode(pos);

        CmsObjectName decoded = new CmsObjectName().decode(new PerInputStream(pos.toByteArray()));
        assertEquals("LD1", decoded.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsObjectName original = new CmsObjectName("LD1/LN1.DO1");
        CmsObjectName cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsObjectName original = new CmsObjectName("LD1/LN1.DO1");
        CmsObjectName cloned = original.copy();
        cloned.set("LD2/LN2.DO2");
        assertEquals("LD1/LN1.DO1", original.get());
        assertEquals("LD2/LN2.DO2", cloned.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsObjectName name = new CmsObjectName("LD1/LN1.DO1");
        assertEquals("ObjectName: LD1/LN1.DO1", name.toString());
    }

    @Test
    @DisplayName("toString empty")
    void toStringEmpty() {
        CmsObjectName name = new CmsObjectName();
        assertEquals("ObjectName: ", name.toString());
    }
}
