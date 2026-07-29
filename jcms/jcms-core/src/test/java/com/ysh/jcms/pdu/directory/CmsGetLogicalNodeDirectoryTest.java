package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.sequence.common.CmsSubReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryTest {

    @Test
    public void request_roundup_with_ld_name() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reference.choice(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName("ld1");
        a.acsiClass(CmsAcsiClass.DATA_OBJECT);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_ln_reference() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest();
        a.reference.choice(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference("lnRef");
        a.acsiClass(CmsAcsiClass.DATA_SET);
        a.referenceAfter("afterRef");
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
