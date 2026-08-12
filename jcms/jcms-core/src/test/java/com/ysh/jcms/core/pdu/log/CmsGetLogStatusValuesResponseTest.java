package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.choice.CmsLogStatusValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.log.CmsLogStatusValue;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetLogStatusValuesResponse a = new CmsGetLogStatusValuesResponse()
            .log(Arrays.asList(
                new CmsLogStatusValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsLogStatusValueChoice().altValue(new CmsLogStatusValue()
                    .oldEntrTm(new CmsBinaryTime().msOfDay(1000L).daysSince1984(5000))
                    .newEntrTm(new CmsBinaryTime().msOfDay(2000L).daysSince1984(5000))
                    .oldEntr("00000001".getBytes())
                    .newEntr("00000002".getBytes()))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLogStatusValuesResponse b = new CmsGetLogStatusValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
