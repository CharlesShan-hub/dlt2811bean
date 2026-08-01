package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryRequestTest {
    @Test
    public void withLdName() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest()
            .reference(new CmsReferenceChoice().altLdName("ld1"))
            .acsiClass(CmsAcsiClass.DATA_OBJECT);
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void withLnReference() {
        CmsGetLogicalNodeDirectoryRequest a = new CmsGetLogicalNodeDirectoryRequest()
            .reference(new CmsReferenceChoice().altLnReference("lnRef"))
            .acsiClass(CmsAcsiClass.DATA_SET)
            .referenceAfter("afterRef");
        byte[] encoded = a.encode();

        CmsGetLogicalNodeDirectoryRequest b = new CmsGetLogicalNodeDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
