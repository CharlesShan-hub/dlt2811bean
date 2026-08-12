package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.sequence.common.CmsFileEntry;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetFileDirectoryResponse a = new CmsGetFileDirectoryResponse()
            .fileEntry(Arrays.asList(
                new CmsFileEntry()
                    .fileName("f1.txt")
                    .fileSize(100L)
                    .lastModified(new CmsUtcTime()
                        .secondsSinceEpoch(1000000L)
                        .fractionOfSecond(0)
                        .timeQuality(new CmsTimeQuality().leap_seconds_known(true)))
                    .checkSum(12345L),
                new CmsFileEntry()
                    .fileName("f2.txt")
                    .fileSize(200L)
                    .lastModified(new CmsUtcTime()
                        .secondsSinceEpoch(2000000L)
                        .fractionOfSecond(0)
                        .timeQuality(new CmsTimeQuality().clock_failure(true)))
                    .checkSum(67890L)))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetFileDirectoryResponse b = new CmsGetFileDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
