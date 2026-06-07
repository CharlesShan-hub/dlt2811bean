package com.ysh.jcms.service.connection;

import com.ysh.jcms.service.connection.CmsAssociateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAssociateRequest")
class CmsAssociateRequestTest {

    @Test
    void roundtrip() {
        CmsAssociateRequest original = new CmsAssociateRequest();
        original.sap_ref.value("cmsServer");
        original.sap_ref_present.value(true);
        original.auth_param_present.value(false);

        byte[] data = original.encode();
        CmsAssociateRequest decoded = new CmsAssociateRequest().decode(data);

        assertEquals("cmsServer", new String(decoded.sap_ref.value()).trim());
        assertEquals(false, decoded.auth_param_present.value());
    }
}
