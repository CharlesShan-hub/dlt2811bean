package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetEditSgValueErrorTest {
    @Test
    public void roundup() {
        CmsSetEditSgValueError a = new CmsSetEditSgValueError()
            .result(Arrays.asList(
                new CmsServiceError(CmsServiceError.NO_ERROR),
                new CmsServiceError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT)));
        byte[] encoded = a.encode();

        CmsSetEditSgValueError b = new CmsSetEditSgValueError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
