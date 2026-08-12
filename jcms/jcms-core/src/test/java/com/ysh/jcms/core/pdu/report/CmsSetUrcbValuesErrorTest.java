package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetUrcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetUrcbValuesError a = new CmsSetUrcbValuesError()
            .result(Arrays.asList(
                new CmsSetUrcbResult().error(CmsServiceError.INSTANCE_IN_USE).resv(CmsServiceError.NO_ERROR),
                new CmsSetUrcbResult().gi(CmsServiceError.ACCESS_VIOLATION)));
        byte[] encoded = a.encode();

        CmsSetUrcbValuesError b = new CmsSetUrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
