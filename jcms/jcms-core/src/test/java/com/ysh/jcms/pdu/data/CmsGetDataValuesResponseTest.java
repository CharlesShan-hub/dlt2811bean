package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.choice.CmsData;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetDataValuesResponse a = new CmsGetDataValuesResponse()
            .value(Arrays.asList(
                new CmsData().alt_boolean(true),
                new CmsData().alt_int32(42)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetDataValuesResponse b = new CmsGetDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
