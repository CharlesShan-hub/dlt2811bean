package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetBrcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetBrcbValuesRequest a = new CmsGetBrcbValuesRequest()
            .reference(Arrays.asList(
                new CmsObjectReference("brcbRef1"),
                new CmsObjectReference("brcbRef2")));
        byte[] encoded = a.encode();

        CmsGetBrcbValuesRequest b = new CmsGetBrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
