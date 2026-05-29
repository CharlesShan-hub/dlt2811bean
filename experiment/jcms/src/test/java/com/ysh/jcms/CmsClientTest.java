package com.ysh.jcms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsClientTest {

    @Test
    void associateRequestRoundtrip() {
        AssociateRequest original = new AssociateRequest(1001, "cmsServer", false);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        AssociateRequest decoded = AssociateRequest.decode(apdu);
        assertEquals(original.getReqId(), decoded.getReqId());
        assertEquals(original.getServerAccessPointReference(), decoded.getServerAccessPointReference());
        assertEquals(original.isHasAuthenticationParameter(), decoded.isHasAuthenticationParameter());
    }

    @Test
    void releaseRequestRoundtrip() {
        ReleaseRequest original = new ReleaseRequest(2001);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        ReleaseRequest decoded = ReleaseRequest.decode(apdu);
        assertEquals(original.getReqId(), decoded.getReqId());
    }

    @Test
    void abortRoundtrip() {
        Abort original = new Abort(3001, 2);
        byte[] apdu = original.encode();
        assertNotNull(apdu);
        assertTrue(apdu.length > 0);

        Abort decoded = Abort.decode(apdu);
        assertEquals(original.getReqId(), decoded.getReqId());
        assertEquals(original.getAbortReason(), decoded.getAbortReason());
    }
}
