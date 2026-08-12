package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.choice.CmsGocbValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetGoCbValuesResponse a = new CmsGetGoCbValuesResponse()
            .gocb(Arrays.asList(
                new CmsGocbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsGocbValueChoice().altValue(new CmsGoCb()
                    .goEna(true)
                    .goID("goID1")
                    .datSet("dsRef")
                    .confRev(5L)
                    .ndsCom(false))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetGoCbValuesResponse b = new CmsGetGoCbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
