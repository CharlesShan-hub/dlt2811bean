package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteDataSetRequestTest {
    @Test
    public void roundup() {
        CmsDeleteDataSetRequest a = new CmsDeleteDataSetRequest().datasetReference("dsRef");
        byte[] encoded = a.encode();

        CmsDeleteDataSetRequest b = new CmsDeleteDataSetRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
