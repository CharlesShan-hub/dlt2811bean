package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerChoice")
class PerChoiceTest {

    // ==================== Non-extensible ====================

    @Test
    @DisplayName("non-extensible: index 0")
    void index0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, PerChoice.decode(pis));
    }

    @Test
    @DisplayName("non-extensible: index 5")
    void index5() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encode(pos, 5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(5, PerChoice.decode(pis));
    }

    @Test
    @DisplayName("non-extensible: index 100 (via semi-constrained)")
    void index100() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encode(pos, 100);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(100, PerChoice.decode(pis));
    }

    @Test
    @DisplayName("non-extensible: negative index throws")
    void negativeIndex_throws() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerChoice.encode(pos, -1));
    }

    // ==================== Extensible ====================

    @Test
    @DisplayName("extensible: root index 3")
    void extensible_root() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encodeExtensible(pos, false, 3);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerChoice.ChoiceResult result = PerChoice.decodeExtensible(pis);
        assertFalse(result.isExtension);
        assertEquals(3, result.index);
    }

    @Test
    @DisplayName("extensible: extension index 7")
    void extensible_extension() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encodeExtensible(pos, true, 7);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerChoice.ChoiceResult result = PerChoice.decodeExtensible(pis);
        assertTrue(result.isExtension);
        assertEquals(7, result.index);
    }

    // ==================== Round-trip with actual value encoding ====================

    @Test
    @DisplayName("round-trip: boolean at index 0, integer at index 1")
    void roundTrip_booleanOrInteger() throws PerDecodeException {
        // Encode: CHOICE { bool, int } — pick bool (index 0)
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encode(pos, 0);
        PerBoolean.encode(pos, true);

        byte[] data = pos.toByteArray();

        // Decode
        PerInputStream pis = new PerInputStream(data);
        int idx = PerChoice.decode(pis);
        assertEquals(0, idx);
        assertTrue(PerBoolean.decode(pis));
    }

    @Test
    @DisplayName("round-trip: integer at index 1, read value")
    void roundTrip_readInteger() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encode(pos, 1);
        PerInteger.encode(pos, 42, 0, 255);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        int idx = PerChoice.decode(pis);
        assertEquals(1, idx);
        assertEquals(42, PerInteger.decode(pis, 0, 255));
    }

    @Test
    @DisplayName("round-trip: extensible with actual value")
    void roundTrip_extensible() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encodeExtensible(pos, true, 10);
        PerInteger.encodeUnconstrained(pos, 999);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerChoice.ChoiceResult result = PerChoice.decodeExtensible(pis);
        assertTrue(result.isExtension);
        assertEquals(10, result.index);
        assertEquals(999, PerInteger.decodeUnconstrained(pis));
    }

    // ==================== Edge cases ====================

    @Test
    @DisplayName("extensible: root alternative index 0 with value")
    void extensible_rootIndex0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerChoice.encodeExtensible(pos, false, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerChoice.ChoiceResult result = PerChoice.decodeExtensible(pis);
        assertFalse(result.isExtension);
        assertEquals(0, result.index);
    }
}
