package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.goose.CmsSetGoCbResult;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetGoCbValuesErrorTest {
    @Test
    public void roundup() {
        CmsSetGoCbValuesError a = new CmsSetGoCbValuesError()
            .result(Arrays.asList(
                new CmsSetGoCbResult().error(CmsServiceError.NO_ERROR).goEna(CmsServiceError.ACCESS_VIOLATION),
                new CmsSetGoCbResult().goID(CmsServiceError.INSTANCE_NOT_AVAILABLE)));
        byte[] encoded = a.encode();

        CmsSetGoCbValuesError b = new CmsSetGoCbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
