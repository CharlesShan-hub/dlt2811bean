package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetEditSgValueRequestTest {
    @Test
    public void roundup() {
        CmsSetEditSgValueRequest a = new CmsSetEditSgValueRequest()
            .data(Arrays.asList(
                new CmsSgRefValueEntry().reference("ref1").value(new CmsData().alt_boolean(true)),
                new CmsSgRefValueEntry().reference("ref2").value(new CmsData().alt_int32(7))));
        byte[] encoded = a.encode();

        CmsSetEditSgValueRequest b = new CmsSetEditSgValueRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
