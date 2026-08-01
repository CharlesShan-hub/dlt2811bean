package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.data.scalar.CmsFC;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetAllDataValuesRequest a = new CmsGetAllDataValuesRequest()
            .reference(new CmsReferenceChoice().altLnReference("lnRef"))
            .fc(CmsFC.MX);
        byte[] encoded = a.encode();

        CmsGetAllDataValuesRequest b = new CmsGetAllDataValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
