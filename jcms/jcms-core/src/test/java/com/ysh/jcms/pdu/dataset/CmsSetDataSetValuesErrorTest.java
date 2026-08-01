package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetDataSetValuesError a = new CmsSetDataSetValuesError()
            .result(Arrays.asList(CmsServiceError.NO_ERROR, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT));
        byte[] encoded = a.encode();

        CmsSetDataSetValuesError b = new CmsSetDataSetValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
