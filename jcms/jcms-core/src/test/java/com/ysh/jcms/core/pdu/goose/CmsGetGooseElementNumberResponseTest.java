package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.scalar.CmsInt16U;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGooseElementNumberResponseTest {
    @Test
    public void roundup() {
        CmsGetGooseElementNumberResponse a = new CmsGetGooseElementNumberResponse()
            .gocbReference("goref")
            .confRev(5L)
            .datSet("dsRef")
            .memberOffset(Arrays.asList(
                new CmsInt16U().value(1),
                new CmsInt16U().value(2)));
        byte[] encoded = a.encode();

        CmsGetGooseElementNumberResponse b = new CmsGetGooseElementNumberResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
