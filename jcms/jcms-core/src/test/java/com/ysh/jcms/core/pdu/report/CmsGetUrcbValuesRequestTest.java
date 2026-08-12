package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetUrcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetUrcbValuesRequest a = new CmsGetUrcbValuesRequest()
            .reference(Arrays.asList(
                new CmsObjectReference("urcbRef1"),
                new CmsObjectReference("urcbRef2")));
        byte[] encoded = a.encode();

        CmsGetUrcbValuesRequest b = new CmsGetUrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
