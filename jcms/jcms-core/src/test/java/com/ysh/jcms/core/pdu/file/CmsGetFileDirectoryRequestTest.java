package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryRequestTest {
    @Test
    public void roundup() {
        CmsGetFileDirectoryRequest a = new CmsGetFileDirectoryRequest()
            .pathName("/dir")
            .startTime(new CmsUtcTime().secondsSinceEpoch(1000000L).fractionOfSecond(0))
            .stopTime(new CmsUtcTime().secondsSinceEpoch(2000000L).fractionOfSecond(0))
            .fileAfter("f1.txt");
        byte[] encoded = a.encode();

        CmsGetFileDirectoryRequest b = new CmsGetFileDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
