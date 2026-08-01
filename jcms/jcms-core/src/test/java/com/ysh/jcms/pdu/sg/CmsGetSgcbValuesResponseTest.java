package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.choice.CmsSgcbValueChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsSgcb;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetSgcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSgcb sg = new CmsSgcb().numOfSG(3).actSG(2).editSG(1).resvTms(5);
        sg.tActEdt.value(new CmsUtcTime().secondsSinceEpoch(1000000L).fractionOfSecond(0));
        CmsGetSgcbValuesResponse a = new CmsGetSgcbValuesResponse()
            .sgscb(Arrays.asList(
                new CmsSgcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsSgcbValueChoice().altValue(sg)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetSgcbValuesResponse b = new CmsGetSgcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
