package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetLogicalDeviceDirectoryError a = new CmsGetLogicalDeviceDirectoryError(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryError b = new CmsGetLogicalDeviceDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
