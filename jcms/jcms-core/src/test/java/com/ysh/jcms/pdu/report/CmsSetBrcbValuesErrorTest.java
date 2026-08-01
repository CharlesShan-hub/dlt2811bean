package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetBrcbValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetBrcbValuesError a = new CmsSetBrcbValuesError()
            .result(Arrays.asList(
                new CmsSetBrcbResult().error(CmsServiceError.NO_ERROR).rptID(CmsServiceError.ACCESS_VIOLATION),
                new CmsSetBrcbResult().gi(CmsServiceError.INSTANCE_NOT_AVAILABLE)));
        byte[] encoded = a.encode();

        CmsSetBrcbValuesError b = new CmsSetBrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
