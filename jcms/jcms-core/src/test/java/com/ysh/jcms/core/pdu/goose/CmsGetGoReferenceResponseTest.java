package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.goose.CmsGoRefFcEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoReferenceResponseTest {
    @Test
    public void roundup() {
        CmsGetGoReferenceResponse a = new CmsGetGoReferenceResponse()
            .gocbReference("goref")
            .confRev(5L)
            .datSet("dsRef")
            .memberData(Arrays.asList(
                new CmsGoRefFcEntry().reference("ref1").fc(CmsFC.ST),
                new CmsGoRefFcEntry().reference("ref2").fc(CmsFC.MX)));
        byte[] encoded = a.encode();

        CmsGetGoReferenceResponse b = new CmsGetGoReferenceResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
