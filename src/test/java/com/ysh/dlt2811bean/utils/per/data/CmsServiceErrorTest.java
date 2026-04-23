package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
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
}
