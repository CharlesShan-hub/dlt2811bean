package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetLogStatusValuesRequest a = new CmsGetLogStatusValuesRequest()
            .logReference(Arrays.asList(
                new CmsObjectReference("logRef1"),
                new CmsObjectReference("logRef2")));
        byte[] encoded = a.encode();

        CmsGetLogStatusValuesRequest b = new CmsGetLogStatusValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
