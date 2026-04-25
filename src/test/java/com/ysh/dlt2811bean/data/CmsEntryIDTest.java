package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.string.CmsEntryID;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryID")
class CmsEntryIDTest {

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsEntryID id = new CmsEntryID();
        assertArrayEquals(new byte[CmsEntryID.SIZE], id.get());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        byte[] value = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryID id = new CmsEntryID(value);
        assertArrayEquals(value, id.get());
    }

    @Test
    @DisplayName("constructor with null value throws")
    void constructorWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new CmsEntryID(null));
    }

    @Test
    @DisplayName("constructor with wrong length throws")
    void constructorWithWrongLength() {
        assertThrows(IllegalArgumentException.class, () -> new CmsEntryID(new byte[4]));
    }

    @Test
    @DisplayName("set valid value")
    void setValidValue() {
        byte[] value = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryID id = new CmsEntryID();
        id.set(value);
        assertArrayEquals(value, id.get());
    }

    @Test
    @DisplayName("set wrong length throws")
    void setWrongLength() {
        CmsEntryID id = new CmsEntryID();
        assertThrows(IllegalArgumentException.class, () -> id.set(new byte[3]));
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        byte[] value = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryID id = new CmsEntryID(value);

        PerOutputStream pos = new PerOutputStream();
        id.encode(pos);

        CmsEntryID decoded = new CmsEntryID().decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(value, decoded.get());
    }

    @Test
    @DisplayName("encode/decode default value")
    void encodeDecodeDefault() throws Exception {
        CmsEntryID id = new CmsEntryID();

        PerOutputStream pos = new PerOutputStream();
        id.encode(pos);

        CmsEntryID decoded = new CmsEntryID().decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(new byte[CmsEntryID.SIZE], decoded.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        byte[] value = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryID original = new CmsEntryID(value);
        CmsEntryID cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        byte[] value = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        CmsEntryID original = new CmsEntryID(value);
        CmsEntryID cloned = original.copy();
        cloned.get()[0] = (byte) 0xFF;
        assertArrayEquals(value, original.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsEntryID id = new CmsEntryID();
        assertEquals("EntryID: [00 00 00 00 00 00 00 00]", id.toString());
    }
}
