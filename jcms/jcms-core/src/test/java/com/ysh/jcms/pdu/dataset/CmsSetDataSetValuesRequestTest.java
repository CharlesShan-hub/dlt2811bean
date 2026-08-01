package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.choice.CmsData;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesRequestTest {
    @Test
    public void roundup() {
        CmsSetDataSetValuesRequest a = new CmsSetDataSetValuesRequest()
            .datasetReference("dsRef")
            .referenceAfter("after")
            .value(Arrays.asList(
                new CmsData().alt_boolean(true),
                new CmsData().alt_float32(1.5f)));
        byte[] encoded = a.encode();

        CmsSetDataSetValuesRequest b = new CmsSetDataSetValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
