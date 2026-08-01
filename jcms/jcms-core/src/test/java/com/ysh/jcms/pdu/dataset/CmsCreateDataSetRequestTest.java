package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCreateDataSetRequestTest {
    @Test
    public void roundup() {
        CmsCreateDataSetRequest a = new CmsCreateDataSetRequest()
            .datasetReference("dsRef")
            .referenceAfter("after")
            .memberData(Arrays.asList(
                new CmsDataRefFcEntry().reference("ref1".getBytes()).fc(CmsFC.MX),
                new CmsDataRefFcEntry().reference("ref2".getBytes()).fc(CmsFC.SP)));
        byte[] encoded = a.encode();

        CmsCreateDataSetRequest b = new CmsCreateDataSetRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
