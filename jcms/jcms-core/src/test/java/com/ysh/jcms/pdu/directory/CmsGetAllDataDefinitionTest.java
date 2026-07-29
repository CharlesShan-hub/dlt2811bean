package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionTest {

    @Test
    public void request_roundup_without_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reference.choice(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName("ld1");
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_all_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reference.choice(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference("lnRef");
        a.fc(CmsFC.ST);
        a.referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataDefinitionError a = new CmsGetAllDataDefinitionError(CmsServiceError.TYPE_CONFLICT);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionError b = new CmsGetAllDataDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
