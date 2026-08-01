package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.choice.CmsData;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetDataSetValuesResponse a = new CmsGetDataSetValuesResponse()
            .value(Arrays.asList(
                new CmsData().alt_boolean(true),
                new CmsData().alt_int32(42)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetDataSetValuesResponse b = new CmsGetDataSetValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
