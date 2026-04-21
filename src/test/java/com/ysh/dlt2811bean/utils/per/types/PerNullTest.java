package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerNull")
class PerNullTest {

    @Test
    @DisplayName("encode+decode produces empty byte array")
    void roundTrip() {
        PerOutputStream pos = new PerOutputStream();
        PerNull.encode(pos);

        byte[] data = pos.toByteArray();
        assertEquals(0, data.length);

        PerInputStream pis = new PerInputStream(data);
        PerNull.decode(pis); // should not throw
    }

    @Test
    @DisplayName("encode does not write any bits")
    void noBitsWritten() {
        PerOutputStream pos = new PerOutputStream();
        PerNull.encode(pos);
        assertEquals(0, pos.getBitLength());
    }

    @Test
    @DisplayName("multiple NULL encodes produce empty array")
    void multipleNulls() {
        PerOutputStream pos = new PerOutputStream();
        PerNull.encode(pos);
        PerNull.encode(pos);
        PerNull.encode(pos);
        assertEquals(0, pos.getBitLength());
    }

    @Test
    @DisplayName("NULL can be mixed with other types")
    void mixedWithOthers() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);
        PerNull.encode(pos);
        PerBoolean.encode(pos, false);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
        PerNull.decode(pis);
        assertFalse(PerBoolean.decode(pis));
    }
}
