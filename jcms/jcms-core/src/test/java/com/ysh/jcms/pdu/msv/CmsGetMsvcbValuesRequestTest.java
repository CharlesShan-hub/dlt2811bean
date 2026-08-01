package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetMsvcbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetMsvcbValuesRequest a = new CmsGetMsvcbValuesRequest()
            .reference(Arrays.asList(
                new CmsObjectReference("msvcbRef1"),
                new CmsObjectReference("msvcbRef2")));
        byte[] encoded = a.encode();

        CmsGetMsvcbValuesRequest b = new CmsGetMsvcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
