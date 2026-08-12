package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.core.data.bitarray.CmsMsvcbOptFlds;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.choice.CmsMsvcbValueChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.enumerate.CmsSmpMod;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetMsvcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsMsvcb ms = new CmsMsvcb()
            .svEna(true)
            .msvID("msvID1")
            .datSet("ds1")
            .confRev(5L)
            .smpMod(CmsSmpMod.SECONDS_PER_SAMPLE)
            .smpRate(4000)
            .optFlds(new CmsMsvcbOptFlds().sample_rate(true).data_set_name(true));
        CmsGetMsvcbValuesResponse a = new CmsGetMsvcbValuesResponse()
            .msvcb(Arrays.asList(
                new CmsMsvcbValueChoice().altError(CmsServiceError.INSTANCE_IN_USE),
                new CmsMsvcbValueChoice().altValue(ms)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetMsvcbValuesResponse b = new CmsGetMsvcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
