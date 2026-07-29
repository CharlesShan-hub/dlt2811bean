package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsObjectClass;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryTest {

    @Test
    public void request_roundup() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest().objectClass(CmsObjectClass.LOGICAL_DEVICE);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ref_after() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest().objectClass(CmsObjectClass.FILE_SYSTEM)
                .referenceAfter("myRef");
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetServerDirectoryResponse a = new CmsGetServerDirectoryResponse();
        /* SEQUENCE OF ObjectReference — 2 个元素 */
        CmsObjectReference ref1 = new CmsObjectReference("device1");
        CmsObjectReference ref2 = new CmsObjectReference("device2");
        a.reference.add(ref1);
        a.reference.add(ref2);
        a.moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryResponse b = new CmsGetServerDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetServerDirectoryError a = new CmsGetServerDirectoryError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();
        CmsGetServerDirectoryError b = new CmsGetServerDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
