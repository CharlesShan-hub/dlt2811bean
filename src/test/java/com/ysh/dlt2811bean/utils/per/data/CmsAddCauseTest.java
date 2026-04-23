package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
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
}
