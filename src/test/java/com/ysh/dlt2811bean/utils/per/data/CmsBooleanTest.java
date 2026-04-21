package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    @DisplayName("encode+decode true")
    void roundTrip_true() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsBoolean.encode(pos, true);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(CmsBoolean.decode(pis).isValue());
    }

    @Test
    @DisplayName("encode+decode false")
    void roundTrip_false() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsBoolean.encode(pos, false);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertFalse(CmsBoolean.decode(pis).isValue());
    }

    @Test
    @DisplayName("multiple booleans in sequence")
    void multipleBooleans() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsBoolean.encode(pos, true);
        CmsBoolean.encode(pos, false);
        CmsBoolean.encode(pos, true);
        CmsBoolean.encode(pos, true);
        CmsBoolean.encode(pos, false);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(CmsBoolean.decode(pis).isValue());
        assertFalse(CmsBoolean.decode(pis).isValue());
        assertTrue(CmsBoolean.decode(pis).isValue());
        assertTrue(CmsBoolean.decode(pis).isValue());
        assertFalse(CmsBoolean.decode(pis).isValue());
    }

    @Test
    @DisplayName("8 booleans pack into 1 byte")
    void packIntoByte() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        for (int i = 0; i < 8; i++) {
            CmsBoolean.encode(pos, i % 2 == 0);
        }
        assertEquals(1, pos.getByteLength());

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        for (int i = 0; i < 8; i++) {
            assertEquals(i % 2 == 0, CmsBoolean.decode(pis).isValue());
        }
    }
}
