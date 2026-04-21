package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerObjectIdentifier")
class PerObjectIdentifierTest {

    // ==================== Encode/Decode round-trip ====================

    @Test
    @DisplayName("round-trip: 1.3.6.1 (iso.org.dod.internet)")
    void roundTrip_simple() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, new int[]{1, 3, 6, 1});

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        int[] result = PerObjectIdentifier.decode(pis);
        assertArrayEquals(new int[]{1, 3, 6, 1}, result);
    }

    @Test
    @DisplayName("round-trip: 0.4 (itu-t)")
    void roundTrip_0_4() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, new int[]{0, 4});

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertArrayEquals(new int[]{0, 4}, PerObjectIdentifier.decode(pis));
    }

    @Test
    @DisplayName("round-trip: 2.16 (joint-iso-itu-t)")
    void roundTrip_2_16() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, new int[]{2, 16});

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertArrayEquals(new int[]{2, 16}, PerObjectIdentifier.decode(pis));
    }

    @Test
    @DisplayName("round-trip: large sub-identifier (>127)")
    void roundTrip_largeSubId() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, new int[]{1, 3, 6, 1, 1000, 99999});

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertArrayEquals(new int[]{1, 3, 6, 1, 1000, 99999}, PerObjectIdentifier.decode(pis));
    }

    @Test
    @DisplayName("round-trip: empty OID")
    void roundTrip_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, new int[0]);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertArrayEquals(new int[0], PerObjectIdentifier.decode(pis));
    }

    @Test
    @DisplayName("encode: null components")
    void encode_null() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerObjectIdentifier.encode(pos, null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertArrayEquals(new int[0], PerObjectIdentifier.decode(pis));
    }

    @Test
    @DisplayName("encode: invalid first component throws")
    void encode_invalidFirst() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerObjectIdentifier.encode(pos, new int[]{3, 0}));
    }

    // ==================== String conversion ====================

    @Test
    @DisplayName("toString: dotted format")
    void toString_dotted() {
        assertEquals("1.3.6.1", PerObjectIdentifier.toString(new int[]{1, 3, 6, 1}));
    }

    @Test
    @DisplayName("toString: empty array")
    void toString_empty() {
        assertEquals("", PerObjectIdentifier.toString(new int[0]));
    }

    @Test
    @DisplayName("toString: null array")
    void toString_null() {
        assertEquals("", PerObjectIdentifier.toString(null));
    }

    @Test
    @DisplayName("fromString: parse dotted format")
    void fromString_parse() {
        assertArrayEquals(new int[]{1, 3, 6, 1},
            PerObjectIdentifier.fromString("1.3.6.1"));
    }

    @Test
    @DisplayName("fromString: empty string")
    void fromString_empty() {
        assertArrayEquals(new int[0], PerObjectIdentifier.fromString(""));
    }

    @Test
    @DisplayName("fromString: null")
    void fromString_null() {
        assertArrayEquals(new int[0], PerObjectIdentifier.fromString(null));
    }

    @Test
    @DisplayName("fromString + toString round-trip")
    void fromString_toString_roundTrip() {
        String original = "1.3.6.1.4.1";
        int[] parsed = PerObjectIdentifier.fromString(original);
        String result = PerObjectIdentifier.toString(parsed);
        assertEquals(original, result);
    }
}
