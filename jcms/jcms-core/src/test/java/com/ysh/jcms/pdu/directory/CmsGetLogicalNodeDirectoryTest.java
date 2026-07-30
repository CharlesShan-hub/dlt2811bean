package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryTest {

    @Test
    public void request_roundup_with_ld_name() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest()
            .reference(new CmsReferenceChoice().altLdName("ld1"))
            .acsiClass(CmsAcsiClass.DATA_OBJECT);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ln_reference() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest()
            .reference(new CmsReferenceChoice().altLnReference("lnRef"))
            .acsiClass(CmsAcsiClass.DATA_SET)
            .referenceAfter("afterRef");
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void response_roundup_with_array() {
        CmsGetLogicalNodeDirectoryResponse a = new CmsGetLogicalNodeDirectoryResponse();
        a.reference.add(new CmsSubReference("fc"));
        a.reference.add(new CmsSubReference("mx"));
        a.moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryResponse b = new CmsGetLogicalNodeDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetLogicalNodeDirectoryError a = new CmsGetLogicalNodeDirectoryError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryError b = new CmsGetLogicalNodeDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
