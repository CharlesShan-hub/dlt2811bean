package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionRequestTest {
    @Test
    public void roundup() {
        CmsGetDataDefinitionRequest a = new CmsGetDataDefinitionRequest().data(Arrays.asList(
            new CmsDataRefEntry().reference("def1".getBytes()).fc(CmsFC.SV),
            new CmsDataRefEntry().reference("def2".getBytes())));
        byte[] encoded = a.encode();

        CmsGetDataDefinitionRequest b = new CmsGetDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
