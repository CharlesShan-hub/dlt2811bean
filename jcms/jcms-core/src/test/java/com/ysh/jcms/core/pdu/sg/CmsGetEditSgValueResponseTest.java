package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.core.data.choice.CmsData;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetEditSgValueResponseTest {
    @Test
    public void roundup() {
        CmsGetEditSgValueResponse a = new CmsGetEditSgValueResponse()
            .value(Arrays.asList(
                new CmsData().alt_boolean(true),
                new CmsData().alt_int32(7)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetEditSgValueResponse b = new CmsGetEditSgValueResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
