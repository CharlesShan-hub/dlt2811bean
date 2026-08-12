package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetLcbValuesResponse a = new CmsGetLcbValuesResponse()
            .lcb(Arrays.asList(
                new CmsLcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsLcbValueChoice().altValue(new CmsLcb()
                    .logEna(true)
                    .datSet("ds1")
                    .trgOps(new CmsTriggerConditions().data_change(true).integrity(true))
                    .intgPd(30L)
                    .logRef("log1")
                    .optFlds(new CmsLcbOptFlds().bit0(true))
                    .bufTm(60L))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetLcbValuesResponse b = new CmsGetLcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
