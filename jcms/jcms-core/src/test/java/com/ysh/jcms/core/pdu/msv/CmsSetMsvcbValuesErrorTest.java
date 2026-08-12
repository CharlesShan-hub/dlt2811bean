package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.msv.CmsSetMsvcbResult;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetMsvcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetMsvcbValuesError a = new CmsSetMsvcbValuesError()
            .result(Arrays.asList(
                new CmsSetMsvcbResult().error(CmsServiceError.NO_ERROR).svEna(CmsServiceError.ACCESS_VIOLATION),
                new CmsSetMsvcbResult().smpRate(CmsServiceError.INSTANCE_NOT_AVAILABLE)));
        byte[] encoded = a.encode();

        CmsSetMsvcbValuesError b = new CmsSetMsvcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
