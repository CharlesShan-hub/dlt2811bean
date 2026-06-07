package com.ysh.jcms.service.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.ffi.CmsFFI;
import com.ysh.jcms.ffi.CmsType;
import com.ysh.jcms.service.other.CmsAssociationId;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DirectTest {

    @Test
    void encodeAbortDirectly() {
        // Build a struct with the right layout manually
        CmsAbort a = new CmsAbort();
        a.assocId.value("assoc-1");
        a.reason.value(3); // INVALID_ARGUMENT

        byte[] buf = new byte[128];
        IntByReference outLen = new IntByReference(buf.length);
        a.write();

        // Call directly via INSTANCE
        int rc = CmsFFI.INSTANCE.cms_abort_encode(a, buf, outLen);
        System.out.println("rc=" + rc + " outLen=" + outLen.getValue());
        if (rc == 0) {
            byte[] result = Arrays.copyOf(buf, outLen.getValue());
            System.out.println("Encoded " + result.length + " bytes");

            // Decode
            CmsAbort d = new CmsAbort();
            rc = CmsFFI.INSTANCE.cms_abort_decode(d, result, result.length);
            d.read();
            System.out.println("decode rc=" + rc);
            System.out.println("assocId bytes: " + new String(d.assocId.value()).trim());
            System.out.println("reason: " + d.reason.value());
            assertEquals("assoc-1", new String(d.assocId.value()).trim());
            assertEquals(3, d.reason.value());
        }
    }
}
