package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.SclAccessPoint;
import com.ysh.dlt2811bean.scl.model.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclLN;
import com.ysh.dlt2811bean.scl.model.SclLDevice;
import com.ysh.dlt2811bean.scl.reader.SclReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SclReaderTest {

    @Test
    void testParseFullScd() throws Exception {
        SclReader reader = new SclReader();
        SclDocument doc = reader.read("config/sample-scd-full.scd");

        assertNotNull(doc);
        assertNotNull(doc.getHeader());
        assertNotNull(doc.getHeader().getId());

        assertNotNull(doc.getSubstation());

        assertNotNull(doc.getCommunication());

        assertNotNull(doc.getIeds());
        assertFalse(doc.getIeds().isEmpty());
        for (SclIED ied : doc.getIeds()) {
            for (SclAccessPoint ap : ied.getAccessPoints()) {
                if (ap.getServer() != null) {
                    for (SclLDevice ld : ap.getServer().getLDevices()) {
                        SclLN ln0 = ld.getLn0();
                        if (ln0 != null) {
                            assertNotNull(ln0.getLnType());
                        }
                    }
                }
            }
        }

        assertNotNull(doc.getDataTypeTemplates());
    }

    @Test
    void testParseRelayScd() throws Exception {
        SclReader reader = new SclReader();
        SclDocument doc = reader.read("config/sample-scd-relay.scd");

        assertNotNull(doc);
        assertNotNull(doc.getHeader());
        assertNotNull(doc.getHeader().getId());

        assertNotNull(doc.getIeds());
        assertFalse(doc.getIeds().isEmpty());
        for (SclIED ied : doc.getIeds()) {
            for (SclAccessPoint ap : ied.getAccessPoints()) {
                if (ap.getServer() != null) {
                    for (SclLDevice ld : ap.getServer().getLDevices()) {
                        SclLN ln0 = ld.getLn0();
                        if (ln0 != null) {
                            assertNotNull(ln0.getLnType());
                        }
                        for (SclLN ln : ld.getLns()) {
                            if (ln.getInputs() != null && !ln.getInputs().isEmpty()) {
                                assertFalse(ln.getInputs().get(0).getExtRefs().isEmpty());
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(doc.getDataTypeTemplates());
    }
}
