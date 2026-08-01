package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.scalar.CmsInt16U;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoReferenceRequestTest {
    @Test
    public void roundup() {
        CmsGetGoReferenceRequest a = new CmsGetGoReferenceRequest()
            .gocbReference("goref")
            .memberOfs(Arrays.asList(
                new CmsInt16U().value(1),
                new CmsInt16U().value(2)));
        byte[] encoded = a.encode();

        CmsGetGoReferenceRequest b = new CmsGetGoReferenceRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
