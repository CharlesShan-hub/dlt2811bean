package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.sg.CmsSgRefFcEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetEditSgValueRequestTest {
    @Test
    public void roundup() {
        CmsGetEditSgValueRequest a = new CmsGetEditSgValueRequest()
            .data(Arrays.asList(
                new CmsSgRefFcEntry().reference("ref1").fc(CmsFC.ST),
                new CmsSgRefFcEntry().reference("ref2").fc(CmsFC.MX)));
        byte[] encoded = a.encode();

        CmsGetEditSgValueRequest b = new CmsGetEditSgValueRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
