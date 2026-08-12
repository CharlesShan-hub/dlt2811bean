package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSendGooseMessageTest {
    @Test
    public void roundup() {
        CmsSendGooseMessage a = new CmsSendGooseMessage()
            .goID("goID1")
            .datSet("dsRef")
            .goRef("goRef1")
            .t(new CmsUtcTime().secondsSinceEpoch(1000000L).fractionOfSecond(0))
            .stNum(1L)
            .sqNum(100L)
            .simulation(false)
            .confRev(5L)
            .ndsCom(false)
            .data(Arrays.asList(
                new CmsData().alt_boolean(true),
                new CmsData().alt_int32(7)));
        byte[] encoded = a.encode();

        CmsSendGooseMessage b = new CmsSendGooseMessage();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
