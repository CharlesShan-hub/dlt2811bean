package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.sequence.data.CmsDataDefResultEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionResponseTest {
    @Test
    public void roundup() {
        CmsGetDataDefinitionResponse a = new CmsGetDataDefinitionResponse()
            .data(Arrays.asList(
                new CmsDataDefResultEntry()
                    .cdcType("octet-string")
                    .definition(new CmsDataDefinition().alt_octet_string_len(4)),
                new CmsDataDefResultEntry()
                    .definition(new CmsDataDefinition().alt_visible_string_len(8))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetDataDefinitionResponse b = new CmsGetDataDefinitionResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
