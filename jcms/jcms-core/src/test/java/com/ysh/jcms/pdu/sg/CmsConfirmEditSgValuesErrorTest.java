package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsConfirmEditSgValuesErrorTest {
    @Test
    public void roundup() {
        CmsConfirmEditSgValuesError a = new CmsConfirmEditSgValuesError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsConfirmEditSgValuesError b = new CmsConfirmEditSgValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
