package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsObjectReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryTest {

    @Test
    public void request_roundtrip() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest();
        a.reqId.value(1);
        a.objectClass.value(CmsObjectClass.LOGICAL_DEVICE);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(1, b.reqId.value());
        assertEquals(CmsObjectClass.LOGICAL_DEVICE, b.objectClass.value());
        assertFalse(b.refAfterPresent.value());
    }

    @Test
    public void request_roundtrip_with_ref_after() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest();
        a.reqId.value(2);
        a.objectClass.value(CmsObjectClass.FILE_SYSTEM);
        a.refAfterPresent.value(true);
        a.refAfter.value("myRef".getBytes());
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(2, b.reqId.value());
        assertEquals(CmsObjectClass.FILE_SYSTEM, b.objectClass.value());
        assertTrue(b.refAfterPresent.value());
        assertArrayEquals("myRef".getBytes(), b.refAfter.value());
    }

    @Test
    public void response_roundtrip_with_array() {
        CmsGetServerDirectoryResponse a = new CmsGetServerDirectoryResponse();
        a.reqId.value(10);
        /* SEQUENCE OF ObjectReference — 2 个元素 */
        CmsObjectReference ref1 = new CmsObjectReference("device1".getBytes());
        CmsObjectReference ref2 = new CmsObjectReference("device2".getBytes());
        a.reference.add(ref1).add(ref2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryResponse b = new CmsGetServerDirectoryResponse();
        b.decode(encoded);
        assertEquals(10, b.reqId.value());
        assertFalse(b.moreFollows.value());
        assertEquals(2, b.reference.size());
        assertArrayEquals("device1".getBytes(), b.reference.get(0).value());
        assertArrayEquals("device2".getBytes(), b.reference.get(1).value());
    }

    @Test
    public void error_roundtrip() {
        CmsGetServerDirectoryError a = new CmsGetServerDirectoryError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryError b = new CmsGetServerDirectoryError();
        b.decode(encoded);
        assertEquals(99, b.reqId.value());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, b.serviceError.value());
    }
}
