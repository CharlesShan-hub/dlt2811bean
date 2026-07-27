package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryTest {

    @Test
    public void request_roundup_with_ld_name() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reference.choice.value(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName.value("ld1");
        a.acsiClass.value(CmsAcsiClass.DATA_OBJECT);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ln_reference() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef");
        a.acsiClass.value(CmsAcsiClass.DATA_SET);
        a.refAfter("afterRef");
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetLogicalNodeDirectoryResponse a = new CmsGetLogicalNodeDirectoryResponse();
        /* SEQUENCE OF SubReference — 2 个元素 */
        a.reference.add(new CmsSubReference("fc"));
        a.reference.add(new CmsSubReference("mx"));
        a.moreFollows = false;
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryResponse b = new CmsGetLogicalNodeDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetLogicalNodeDirectoryError a = new CmsGetLogicalNodeDirectoryError().serviceError(com.ysh.jcms.data.common.CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryError b = new CmsGetLogicalNodeDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
