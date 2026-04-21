package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerEnumerated")
class PerEnumeratedTest {

    @Test
    @DisplayName("non-extensible: ordinal 0")
    void roundTrip_ordinal0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerEnumerated.encode(pos, 0, 1);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, PerEnumerated.decode(pis, 1));
    }

    @Test
    @DisplayName("non-extensible: single value (maxOrdinal=0)")
    void singleValue() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerEnumerated.encode(pos, 0, 0);

        PerInputStream pis = new PerInputStream(new byte[0]);
        assertEquals(0, PerEnumerated.decode(pis, 0));
    }

    @Test
    @DisplayName("non-extensible: all ordinals in range 0..3")
    void allOrdinals_0to3() throws PerDecodeException {
        for (int ord = 0; ord <= 3; ord++) {
            PerOutputStream pos = new PerOutputStream();
            PerEnumerated.encode(pos, ord, 3);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(ord, PerEnumerated.decode(pis, 3));
        }
    }

    @Test
    @DisplayName("non-extensible: ordinal out of range throws")
    void outOfRange_throws() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerEnumerated.encode(pos, 5, 3));
    }

    @Test
    @DisplayName("non-extensible: negative ordinal throws")
    void negativeOrdinal_throws() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerEnumerated.encode(pos, -1, 3));
    }

    @Test
    @DisplayName("extensible: root value (not extension)")
    void extensible_rootValue() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerEnumerated.encodeExtensible(pos, false, 1, 2);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerEnumerated.EnumeratedResult result = PerEnumerated.decodeExtensible(pis, 2);
        assertFalse(result.isExtension);
        assertEquals(1, result.ordinal);
    }

    @Test
    @DisplayName("extensible: extension value")
    void extensible_extensionValue() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerEnumerated.encodeExtensible(pos, true, 5, 2);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerEnumerated.EnumeratedResult result = PerEnumerated.decodeExtensible(pis, 2);
        assertTrue(result.isExtension);
        assertEquals(5, result.ordinal);
    }

    @Test
    @DisplayName("extensible: small extension value (0..63)")
    void extensible_smallExtension() throws PerDecodeException {
        for (int ord = 0; ord <= 63; ord++) {
            PerOutputStream pos = new PerOutputStream();
            PerEnumerated.encodeExtensible(pos, true, ord, 2);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            PerEnumerated.EnumeratedResult result = PerEnumerated.decodeExtensible(pis, 2);
            assertTrue(result.isExtension);
            assertEquals(ord, result.ordinal);
        }
    }
}
