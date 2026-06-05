package com.ysh.jcms.service.connection;

import com.ysh.jcms.service.connection.CmsReleaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CmsReleaseResponse")
class CmsReleaseResponseTest {

    @Test
    void roundtrip() {
        CmsReleaseResponse original = new CmsReleaseResponse();
        original.assocId.bytes("assoc-1");

        byte[] data = original.encode();
        new CmsReleaseResponse().decode(data);
    }
}
