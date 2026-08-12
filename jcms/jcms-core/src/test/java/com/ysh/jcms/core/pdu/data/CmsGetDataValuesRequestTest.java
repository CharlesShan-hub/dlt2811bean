package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetDataValuesRequest a = new CmsGetDataValuesRequest().data(Arrays.asList(
            new CmsDataRefEntry().reference("dv1".getBytes()).fc(CmsFC.MX),
            new CmsDataRefEntry().reference("dv2".getBytes())));
        byte[] encoded = a.encode();

        CmsGetDataValuesRequest b = new CmsGetDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
