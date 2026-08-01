package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.sequence.block.CmsUrcb;
import com.ysh.jcms.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetUrcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsUrcb ur = new CmsUrcb()
            .rptID("urcbRpt")
            .rptEna(true)
            .datSet("ds1")
            .confRev(50L)
            .optFlds(new CmsRcbOptFlds().sequence_number(true))
            .bufTm(30L)
            .sqNum(5)
            .trgOps(new CmsTriggerConditions().data_change(true))
            .intgPd(60L)
            .gi(false)
            .resv(false);
        CmsGetUrcbValuesResponse a = new CmsGetUrcbValuesResponse()
            .urcb(Arrays.asList(
                new CmsUrcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsUrcbValueChoice().altValue(ur)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetUrcbValuesResponse b = new CmsGetUrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
