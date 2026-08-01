package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryRequestTest {
    @Test
    public void roundup() {
        CmsGetDataSetDirectoryRequest a = new CmsGetDataSetDirectoryRequest()
            .datasetReference("dsRef")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetDataSetDirectoryRequest b = new CmsGetDataSetDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
