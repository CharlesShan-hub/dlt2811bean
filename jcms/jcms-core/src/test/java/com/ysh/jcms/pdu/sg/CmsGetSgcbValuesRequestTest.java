package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetSgcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetSgcbValuesRequest a = new CmsGetSgcbValuesRequest()
            .sgcbReference(Arrays.asList(
                new CmsObjectReference("sgcbRef1"),
                new CmsObjectReference("sgcbRef2")));
        byte[] encoded = a.encode();

        CmsGetSgcbValuesRequest b = new CmsGetSgcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
