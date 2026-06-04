package com.ysh.jcms.datatypes2.svc;

import com.ysh.jcms.datatypes2.svc.connection.CmsReleaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReleaseRequest")
class CmsReleaseRequestTest {

    @Test
    void roundtrip() {
        CmsReleaseRequest original = new CmsReleaseRequest();
        original.assocId.data.set("assoc-1".getBytes());

        byte[] data = original.encode();
        CmsReleaseRequest decoded = CmsReleaseRequest.from(data);
    }
}
