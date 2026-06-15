package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsObjectReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryTest {

    @Test
    public void request_roundtrip() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest()
            .reqId(1)
            .objectClass(CmsObjectClass.LOGICAL_DEVICE)
            .refAfterPresent(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundtrip_with_ref_after() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest()
            .reqId(2)
            .objectClass(CmsObjectClass.FILE_SYSTEM)
            .refAfterPresent(true)
            .refAfter("myRef".getBytes());
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundtrip_with_array() {
        CmsGetServerDirectoryResponse a = new CmsGetServerDirectoryResponse()
            .reqId(10);
        /* SEQUENCE OF ObjectReference — 2 个元素 */
        CmsObjectReference ref1 = new CmsObjectReference("device1".getBytes());
        CmsObjectReference ref2 = new CmsObjectReference("device2".getBytes());
        a.reference.add(ref1).add(ref2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryResponse b = new CmsGetServerDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundtrip() {
        CmsGetServerDirectoryError a = new CmsGetServerDirectoryError()
            .reqId(99)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryError b = new CmsGetServerDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
