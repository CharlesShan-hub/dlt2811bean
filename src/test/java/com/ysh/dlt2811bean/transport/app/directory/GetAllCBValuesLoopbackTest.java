package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetAllCBValues Loopback Test")
class GetAllCBValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("URCB: returns unbuffered report control values")
    void urcb() throws Exception {
        associate();

        CmsApdu response = client.getAllCBValuesByLd("C1", CmsACSIClass.URCB);
        //log.info("Response (URCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        assertNotNull(asdu.cbValue());
        assertEquals(2, asdu.cbValue().size());

        //log.info("URCB values:");
        //for (int i = 0; i < asdu.cbValue().size(); i++) {
        //    CmsCBValueEntry entry = asdu.cbValue().get(i);
        //    log.info("  [{}] ref={}, value={}", i, entry.reference().get(), entry.value());
        //}

        assertEquals("PosReport", asdu.cbValue().get(0).reference().get());
        assertEquals("MeaReport", asdu.cbValue().get(1).reference().get());
    }

    @Test
    @DisplayName("BRCB: no buffered reports in this SCD")
    void brcb() throws Exception {
        associate();

        CmsApdu response = client.getAllCBValuesByLd("C1", CmsACSIClass.BRCB);
        //log.info("Response (BRCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        assertTrue(asdu.cbValue().isEmpty());
    }

    @Test
    @DisplayName("LCB: returns log control values")
    void lcb() throws Exception {
        associate();

        CmsApdu response = client.getAllCBValuesByLd("C1", CmsACSIClass.LCB);
        //log.info("Response (LCB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        assertNotNull(asdu.cbValue());
        assertEquals(1, asdu.cbValue().size());

        //log.info("LCB values:");
        //for (int i = 0; i < asdu.cbValue().size(); i++) {
        //    CmsCBValueEntry entry = asdu.cbValue().get(i);
        //    log.info("  [{}] ref={}, value={}", i, entry.reference().get(), entry.value());
        //}

        assertEquals("Log", asdu.cbValue().get(0).reference().get());
    }

    @Test
    @DisplayName("GO_CB: returns GOOSE control values")
    void goCb() throws Exception {
        associate();

        CmsApdu response = client.getAllCBValuesByLd("C1", CmsACSIClass.GO_CB);
        //log.info("Response (GO_CB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        assertNotNull(asdu.cbValue());
        assertEquals(1, asdu.cbValue().size());

        //log.info("GO_CB value:");
        CmsCBValueEntry entry = asdu.cbValue().get(0);
        //log.info("  ref={}, value={}", entry.reference().get(), entry.value());

        assertEquals("ItlPositions", entry.reference().get());
    }

    @Test
    @DisplayName("MSV_CB: returns sampled value control values")
    void msvCb() throws Exception {
        associate();

        CmsApdu response = client.getAllCBValuesByLd("C1", CmsACSIClass.MSV_CB);
        //log.info("Response (MSV_CB): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        assertNotNull(asdu.cbValue());
        assertEquals(1, asdu.cbValue().size());

        //log.info("MSV_CB value:");
        CmsCBValueEntry entry = asdu.cbValue().get(0);
        //log.info("  ref={}, value={}", entry.reference().get(), entry.value());

        assertEquals("Volt", entry.reference().get());
    }
}
