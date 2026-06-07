package com.ysh.jcms.service.connection;

import com.ysh.jcms.service.connection.CmsReleaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CmsReleaseRequest")
class CmsReleaseRequestTest {

    @Test
    void roundtrip() {
        CmsReleaseRequest original = new CmsReleaseRequest();
        original.assocId.value("assoc-1");

        byte[] data = original.encode();
        new CmsReleaseRequest().decode(data);
    }
}
