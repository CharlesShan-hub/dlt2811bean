package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAddCause")
class CmsAddCauseTest {

    @Test
    @DisplayName("construct, set, is, encode/decode")
    void testAll() throws Exception {
        CmsAddCause cause = new CmsAddCause();
        assertEquals(CmsAddCause.UNKNOWN, cause.get());

        cause.set(CmsAddCause.BLOCKED_BY_INTERLOCKING);
        assertTrue(cause.is(CmsAddCause.BLOCKED_BY_INTERLOCKING));
        assertFalse(cause.is(CmsAddCause.NOT_SUPPORTED));

        PerOutputStream pos = new PerOutputStream();
        cause.encode(pos);

        CmsAddCause decoded = new CmsAddCause().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsAddCause.BLOCKED_BY_INTERLOCKING, decoded.get());
        assertTrue(decoded.is(CmsAddCause.BLOCKED_BY_INTERLOCKING));
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsAddCause.write(pos, CmsAddCause.NONE);

        CmsAddCause decoded = CmsAddCause.read(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.is(CmsAddCause.NONE));
    }
}
