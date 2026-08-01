package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
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
