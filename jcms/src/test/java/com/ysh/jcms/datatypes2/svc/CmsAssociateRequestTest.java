package com.ysh.jcms.datatypes2.svc;

import com.ysh.jcms.datatypes2.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociateRequest")
class CmsAssociateRequestTest {

    @Test
    void roundtrip() {
        CmsAssociateRequest original = new CmsAssociateRequest();
        original.sapRef.set("cmsServer");
        original.hasAuth = new CmsBoolean(false);

        byte[] data = original.encode();
        CmsAssociateRequest decoded = CmsAssociateRequest.from(data);

        assertEquals("cmsServer", decoded.sapRef.get().trim());
        assertFalse(decoded.hasAuth.get());
    }

    @Test
    void withAuth() {
        CmsAssociateRequest original = new CmsAssociateRequest();
        original.sapRef.set("cmsServer");
        original.hasAuth = new CmsBoolean(true);
        original.certLen = new com.ysh.jcms.datatypes2.data.basic.CmsInt32U(5);
        System.arraycopy(new byte[]{1, 2, 3, 4, 5}, 0, original.cert, 0, 5);

        byte[] data = original.encode();
        CmsAssociateRequest decoded = CmsAssociateRequest.from(data);

        assertTrue(decoded.hasAuth.get());
        assertEquals(5, decoded.certLen.longValue());
    }
}
