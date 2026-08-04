package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import org.junit.Test;

public class AllData13TempTest {

    @Test
    public void roundtrip_13_entries() {
        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse();
        for (int i = 0; i < 13; i++) {
            CmsDataValueEntry e = new CmsDataValueEntry();
            e.reference("DO" + i);
            switch (i % 4) {
                case 0:
                    e.value(new CmsData().alt_visible_string("v" + i));
                    break;
                case 1:
                    e.value(new CmsData().alt_int32(i));
                    break;
                case 2:
                    e.value(new CmsData().alt_boolean(i % 2 == 0));
                    break;
                default:
                    e.value(new CmsData().alt_float32(i + 0.5f));
                    break;
            }
            resp.data.add(e);
        }
        byte[] data = resp.encode();
        System.out.println("ASDU len=" + data.length);

        CmsGetAllDataValuesResponse r2 = new CmsGetAllDataValuesResponse();
        r2.decode(data);
        System.out.println("entries=" + r2.data.size());
        for (CmsDataValueEntry e : r2.data) {
            int ct = e.value.choice();
            System.out.println("ref=" + e.reference.value() + " choice=" + ct);
        }
    }
}
