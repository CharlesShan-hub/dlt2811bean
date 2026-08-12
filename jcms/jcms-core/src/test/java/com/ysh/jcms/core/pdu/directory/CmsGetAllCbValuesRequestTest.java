package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.choice.CmsReferenceChoice;
import com.ysh.jcms.core.data.enumerate.CmsAcsiClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllCbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetAllCbValuesRequest a = new CmsGetAllCbValuesRequest()
            .reference(new CmsReferenceChoice().altLdName("ld1"))
            .acsiClass(CmsAcsiClass.BRCB)
            .referenceAfter("afterRef");
        byte[] encoded = a.encode();

        CmsGetAllCbValuesRequest b = new CmsGetAllCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
