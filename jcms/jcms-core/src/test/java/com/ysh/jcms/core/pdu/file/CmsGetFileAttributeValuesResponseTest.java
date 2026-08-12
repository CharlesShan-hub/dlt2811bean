package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesResponseTest {
    @Test
    public void roundup() {
        CmsGetFileAttributeValuesResponse a = new CmsGetFileAttributeValuesResponse()
            .fileName("f.txt")
            .fileSize(1024L)
            .lastModified(new CmsUtcTime()
                .secondsSinceEpoch(3000000L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true)))
            .checkSum(999L);
        byte[] encoded = a.encode();

        CmsGetFileAttributeValuesResponse b = new CmsGetFileAttributeValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
