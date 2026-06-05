package com.ysh.jcms.service.connection;

import com.ysh.jcms.service.connection.CmsAbort;
import com.ysh.jcms.service.connection.CmsAbortReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort")
class CmsAbortTest {

    @Test
    void roundtrip() {
        CmsAbort original = new CmsAbort();
        original.reason.value(CmsAbortReason.INVALID_ARGUMENT);
        original.assocId.bytes("assoc-1");

        byte[] data = original.encode();
        CmsAbort decoded = new CmsAbort().decode(data);

        assertEquals(CmsAbortReason.INVALID_ARGUMENT, decoded.reason.value());
    }
}
