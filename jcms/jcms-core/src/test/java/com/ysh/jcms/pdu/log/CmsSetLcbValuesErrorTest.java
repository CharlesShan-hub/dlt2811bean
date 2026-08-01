package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetLcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetLcbValuesError a = new CmsSetLcbValuesError()
            .result(Arrays.asList(
                new CmsSetLcbResult().error(CmsServiceError.NO_ERROR).logEna(CmsServiceError.ACCESS_VIOLATION),
                new CmsSetLcbResult().intgPd(CmsServiceError.INSTANCE_NOT_AVAILABLE)));
        byte[] encoded = a.encode();

        CmsSetLcbValuesError b = new CmsSetLcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
