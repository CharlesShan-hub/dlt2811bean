package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionResponseTest {
    @Test
    public void roundup() {
        CmsGetAllDataDefinitionResponse a = new CmsGetAllDataDefinitionResponse()
            .data(Arrays.asList(
                new CmsDataDefinitionEntry()
                    .reference("ref1")
                    .cdcType("INT32")
                    .definition(new CmsDataDefinition().alt_octet_string_len(4)),
                new CmsDataDefinitionEntry()
                    .reference("ref2")
                    .definition(new CmsDataDefinition().alt_visible_string_len(8))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionResponse b = new CmsGetAllDataDefinitionResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
