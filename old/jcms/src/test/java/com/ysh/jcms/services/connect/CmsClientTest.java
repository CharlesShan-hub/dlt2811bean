package com.ysh.jcms.services.connect;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class CmsClientTest {

    @Test
    void associateRequestRoundtrip() {
        CmsAssociateRequest original = new CmsAssociateRequest("cmsServer", false, new byte[0], 0, new byte[0]);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        CmsAssociateRequest decoded = CmsAssociateRequest.from(apdu);
        assertArrayEquals(original.sapRef(), decoded.sapRef());
        assertEquals(original.hasAuth(), decoded.hasAuth());
    }

    @Test
    void releaseRequestRoundtrip() {
        CmsReleaseRequest original = new CmsReleaseRequest("assoc-1".getBytes(StandardCharsets.UTF_8));
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        CmsReleaseRequest decoded = CmsReleaseRequest.from(apdu);
        assertArrayEquals(original.assocId().get(), decoded.assocId().get());
    }

    @Test
    void abortRoundtrip() {
        CmsAbort original = new CmsAbort("assoc-1".getBytes(StandardCharsets.UTF_8), 2);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        CmsAbort decoded = CmsAbort.from(apdu);
        assertArrayEquals(original.assocId().get(), decoded.assocId().get());
        assertEquals(original.reason(), decoded.reason());
    }
}
