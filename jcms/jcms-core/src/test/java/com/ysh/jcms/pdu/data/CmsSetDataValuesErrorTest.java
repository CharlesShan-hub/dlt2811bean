package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetDataValuesError a = new CmsSetDataValuesError()
            .result(Arrays.asList(
                CmsServiceError.NO_ERROR,
                CmsServiceError.ACCESS_VIOLATION));
        byte[] encoded = a.encode();

        CmsSetDataValuesError b = new CmsSetDataValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
