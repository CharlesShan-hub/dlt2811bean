package com.ysh.dlt2811bean.datatypes.enumerated;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsServiceError")
class CmsServiceErrorTest {

    @Test
    @DisplayName("construct, set, is, encode/decode")
    void testAll() throws Exception {
        CmsServiceError err = new CmsServiceError();
        assertEquals(CmsServiceError.NO_ERROR, err.get());

        err.set(CmsServiceError.ACCESS_VIOLATION);
        assertTrue(err.is(CmsServiceError.ACCESS_VIOLATION));
        assertFalse(err.is(CmsServiceError.NO_ERROR));

        PerOutputStream pos = new PerOutputStream();
        err.encode(pos);

        CmsServiceError decoded = new CmsServiceError().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsServiceError.ACCESS_VIOLATION, decoded.get());
        assertTrue(decoded.is(CmsServiceError.ACCESS_VIOLATION));
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsServiceError.write(pos, CmsServiceError.INSTANCE_IN_USE);

        CmsServiceError decoded = CmsServiceError.read(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.is(CmsServiceError.INSTANCE_IN_USE));
    }
}
