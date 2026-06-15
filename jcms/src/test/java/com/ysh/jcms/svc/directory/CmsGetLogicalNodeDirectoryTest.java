package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryTest {

    @Test
    public void request_roundtrip_with_ld_name() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reqId.value(5);
        a.reference.choice.value(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName.value("ld1".getBytes());
        a.acsiClass.value(CmsAcsiClass.DATA_OBJECT);
        a.refAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(5, b.reqId.value());
        assertEquals(CmsReferenceChoice.LD_NAME, b.reference.choice.value());
        assertArrayEquals("ld1".getBytes(), b.reference.altLdName.value());
        assertEquals(CmsAcsiClass.DATA_OBJECT, b.acsiClass.value());
        assertFalse(b.refAfterPresent.value());
    }

    @Test
    public void request_roundtrip_with_ln_reference() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reqId.value(6);
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef".getBytes());
        a.acsiClass.value(CmsAcsiClass.DATA_SET);
        a.refAfterPresent.value(true);
        a.refAfter.value("afterRef".getBytes());
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(6, b.reqId.value());
        assertEquals(CmsReferenceChoice.LN_REFERENCE, b.reference.choice.value());
        assertArrayEquals("lnRef".getBytes(), b.reference.altLnReference.value());
        assertEquals(CmsAcsiClass.DATA_SET, b.acsiClass.value());
        assertTrue(b.refAfterPresent.value());
        assertArrayEquals("afterRef".getBytes(), b.refAfter.value());
    }

    @Test
    public void response_roundtrip_with_array() {
        CmsGetLogicalNodeDirectoryResponse a = new CmsGetLogicalNodeDirectoryResponse();
        a.reqId.value(30);
        /* SEQUENCE OF SubReference — 2 个元素 */
        a.reference.add(new CmsSubReference("fc".getBytes()))
                    .add(new CmsSubReference("mx".getBytes()));
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryResponse b = new CmsGetLogicalNodeDirectoryResponse();
        b.decode(encoded);
        assertEquals(30, b.reqId.value());
        assertFalse(b.moreFollows.value());
        assertEquals(2, b.reference.size());
        assertArrayEquals("fc".getBytes(), b.reference.get(0).value());
        assertArrayEquals("mx".getBytes(), b.reference.get(1).value());
    }

    @Test
    public void error_roundtrip() {
        CmsGetLogicalNodeDirectoryError a = new CmsGetLogicalNodeDirectoryError();
        a.reqId.value(77);
        a.serviceError.value(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryError b = new CmsGetLogicalNodeDirectoryError();
        b.decode(encoded);
        assertEquals(77, b.reqId.value());
        assertEquals(CmsServiceError.ACCESS_VIOLATION, b.serviceError.value());
    }
}
