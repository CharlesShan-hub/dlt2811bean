package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsSmpMod;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSendMsvMessageTest {
    @Test
    public void roundup() {
        CmsSendMsvMessage a = new CmsSendMsvMessage()
            .msvID("msvId")
            .datSet("ds1")
            .smpCnt(1)
            .confRev(5L)
            .refTm(new CmsUtcTime().secondsSinceEpoch(1000L).fractionOfSecond(0))
            .smpSynch(0)
            .smpRate(4000)
            .simulation(false)
            .sample(Arrays.asList(
                new CmsData().alt_int32(100),
                new CmsData().alt_boolean(true)))
            .smpMod(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD);
        byte[] encoded = a.encode();

        CmsSendMsvMessage b = new CmsSendMsvMessage();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
