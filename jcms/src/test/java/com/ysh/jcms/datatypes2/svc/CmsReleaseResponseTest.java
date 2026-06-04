package com.ysh.jcms.datatypes2.svc;

import com.ysh.jcms.datatypes2.svc.connection.CmsReleaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReleaseResponse")
class CmsReleaseResponseTest {

    @Test
    void roundtrip() {
        CmsReleaseResponse original = new CmsReleaseResponse();
        original.assocId.data.set("assoc-1".getBytes());

        byte[] data = original.encode();
        CmsReleaseResponse decoded = CmsReleaseResponse.from(data);
    }
}
