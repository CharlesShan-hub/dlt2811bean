package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.choice.CmsReferenceChoice;
import com.ysh.jcms.core.data.scalar.CmsFC;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionRequestTest {
    @Test
    public void withoutOptional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest()
            .reference(new CmsReferenceChoice().altLdName("ld1"));
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void withAllOptional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest()
            .reference(new CmsReferenceChoice().altLnReference("lnRef"))
            .fc(CmsFC.ST)
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
