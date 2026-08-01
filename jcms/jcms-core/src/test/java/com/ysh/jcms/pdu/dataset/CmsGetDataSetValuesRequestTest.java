package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetDataSetValuesRequest a = new CmsGetDataSetValuesRequest()
            .datasetReference("dsRef")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetDataSetValuesRequest b = new CmsGetDataSetValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
