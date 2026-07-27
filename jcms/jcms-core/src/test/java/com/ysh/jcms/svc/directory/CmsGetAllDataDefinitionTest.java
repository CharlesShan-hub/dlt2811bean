package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionTest {

    @Test
    public void request_roundup_without_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reference.choice.value(CmsReferenceChoice.LD_NAME);
        a.reference.altLdName.value("ld1");
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void request_roundup_with_all_optional() {
        CmsGetAllDataDefinitionRequest a = new CmsGetAllDataDefinitionRequest();
        a.reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
        a.reference.altLnReference.value("lnRef");
        a.fc(CmsFC.ST);
        a.refAfter("after");
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionRequest b = new CmsGetAllDataDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void error_roundup() {
        CmsGetAllDataDefinitionError a = new CmsGetAllDataDefinitionError().serviceError(com.ysh.jcms.data.common.CmsServiceError.TYPE_CONFLICT);
        byte[] encoded = a.encode();

        CmsGetAllDataDefinitionError b = new CmsGetAllDataDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
