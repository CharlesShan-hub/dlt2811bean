package com.ysh.jcms.datatypes2.svc;

import com.ysh.jcms.datatypes2.svc.connection.CmsAbort;
import com.ysh.jcms.datatypes2.svc.connection.CmsAbortReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAbort")
class CmsAbortTest {

    @Test
    void roundtrip() {
        CmsAbort original = new CmsAbort();
        original.reason = CmsAbortReason.INVALID_ARGUMENT;
        original.assocId.data.set("assoc-1".getBytes());

        byte[] data = original.encode();
        CmsAbort decoded = CmsAbort.from(data);

        assertEquals(CmsAbortReason.INVALID_ARGUMENT, decoded.reason);
    }
}
