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

        CmsAssociateRequest decoded = CmsAssociateRequest.decode(apdu);
        assertArrayEquals(original.getSapRef(), decoded.getSapRef());
        assertEquals(original.getHasAuth(), decoded.getHasAuth());
    }

    @Test
    void releaseRequestRoundtrip() {
        CmsReleaseRequest original = new CmsReleaseRequest("assoc-1".getBytes(StandardCharsets.UTF_8));
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        CmsReleaseRequest decoded = CmsReleaseRequest.decode(apdu);
        assertArrayEquals(original.getAssocId(), decoded.getAssocId());
    }

    @Test
    void abortRoundtrip() {
        CmsAbort original = new CmsAbort("assoc-1".getBytes(StandardCharsets.UTF_8), 2);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        CmsAbort decoded = CmsAbort.decode(apdu);
        assertArrayEquals(original.getAssocId(), decoded.getAssocId());
        assertEquals(original.getReason(), decoded.getReason());
    }
}
