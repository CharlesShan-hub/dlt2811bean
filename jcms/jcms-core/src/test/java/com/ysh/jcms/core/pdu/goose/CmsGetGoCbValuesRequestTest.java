package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetGoCbValuesRequest a = new CmsGetGoCbValuesRequest()
            .reference(Arrays.asList(
                new CmsObjectReference("goref1"),
                new CmsObjectReference("goref2")));
        byte[] encoded = a.encode();

        CmsGetGoCbValuesRequest b = new CmsGetGoCbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
