package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.choice.CmsRcbValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetBrcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsBrcb br = new CmsBrcb()
            .rptID("brcbRpt")
            .rptEna(true)
            .datSet("ds1")
            .confRev(100L)
            .optFlds(new CmsRcbOptFlds().sequence_number(true))
            .bufTm(30L)
            .sqNum(5)
            .trgOps(new CmsTriggerConditions().data_change(true))
            .intgPd(60L)
            .gi(false)
            .purgeBuf(true)
            .entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        CmsGetBrcbValuesResponse a = new CmsGetBrcbValuesResponse()
            .brcb(Arrays.asList(
                new CmsRcbValueChoice().altError(CmsServiceError.INSTANCE_IN_USE),
                new CmsRcbValueChoice().altValue(br)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetBrcbValuesResponse b = new CmsGetBrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
