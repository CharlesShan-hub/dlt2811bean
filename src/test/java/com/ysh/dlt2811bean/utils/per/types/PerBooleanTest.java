package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerBoolean")
class PerBooleanTest {

    @Test
    @DisplayName("encode+decode true")
    void roundTrip_true() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
    }

    @Test
    @DisplayName("encode+decode false")
    void roundTrip_false() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, false);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertFalse(PerBoolean.decode(pis));
    }

    @Test
    @DisplayName("multiple booleans in sequence")
    void multipleBooleans() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);
        PerBoolean.encode(pos, false);
        PerBoolean.encode(pos, true);
        PerBoolean.encode(pos, true);
        PerBoolean.encode(pos, false);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
        assertFalse(PerBoolean.decode(pis));
        assertTrue(PerBoolean.decode(pis));
        assertTrue(PerBoolean.decode(pis));
        assertFalse(PerBoolean.decode(pis));
    }

    @Test
    @DisplayName("encode true produces exactly 1 bit")
    void trueIsOneBit() {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);
        assertEquals(1, pos.getBitLength());
    }

    @Test
    @DisplayName("8 booleans pack into 1 byte")
    void packIntoByte() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        for (int i = 0; i < 8; i++) {
            PerBoolean.encode(pos, i % 2 == 0);
        }
        assertEquals(8, pos.getBitLength());
        assertEquals(1, pos.getByteLength());

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        for (int i = 0; i < 8; i++) {
            assertEquals(i % 2 == 0, PerBoolean.decode(pis));
        }
    }

    @Test
    @DisplayName("decode on empty data throws")
    void decodeEmpty_throws() {
        PerInputStream pis = new PerInputStream(new byte[0]);
        assertThrows(PerDecodeException.class, () -> PerBoolean.decode(pis));
    }
}
