package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetLcbValuesRequest a = new CmsGetLcbValuesRequest()
            .reference(Arrays.asList(
                new CmsObjectReference("lcbRef1"),
                new CmsObjectReference("lcbRef2")));
        byte[] encoded = a.encode();

        CmsGetLcbValuesRequest b = new CmsGetLcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
