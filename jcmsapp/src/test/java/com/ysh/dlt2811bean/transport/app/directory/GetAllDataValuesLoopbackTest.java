package com.ysh.dlt2811bean.transport.app.directory;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetAllDataValues Loopback Test")
class GetAllDataValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("by lnReference returns configured data values for that LN")
    void byLnReference() throws Exception {
        associate();

        CmsApdu response = client.getAllDataValuesByLn("C1/LPHD1");
        //log.info("Response (lnReference=C1/LPHD1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        assertNotNull(asdu.data());
        assertEquals(1, asdu.data().size());

        CmsDataEntry entry = asdu.data().get(0);
        assertEquals("Proxy.stVal", entry.reference().get());
        //log.info("  {} = {}", entry.reference().get(), entry.value());
    }

    @Test
    @DisplayName("by lnReference with fc=ST filters correctly")
    void byLnReferenceWithFcSt() throws Exception {
        associate();

        CmsApdu response = client.getAllDataValuesByLn("C1/LPHD1", "ST");
        //log.info("Response (lnReference=C1/LPHD1, fc=ST): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        assertNotNull(asdu.data());
        assertEquals(1, asdu.data().size());

        CmsDataEntry entry = asdu.data().get(0);
        assertEquals("Proxy.stVal", entry.reference().get());
        //log.info("  {} = {}", entry.reference().get(), entry.value());
    }

    @Test
    @DisplayName("by lnReference with fc=MX returns no data (no MX values configured)")
    void byLnReferenceWithFcMx() throws Exception {
        associate();

        CmsApdu response = client.getAllDataValuesByLn("C1/LPHD1", "MX");
        //log.info("Response (lnReference=C1/LPHD1, fc=MX): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        assertTrue(asdu.data().isEmpty());
    }

    @Test
    @DisplayName("by ldName returns configured data values across all LNs")
    void byLdName() throws Exception {
        associate();

        CmsApdu response = client.getAllDataValuesByLd("C1");
        //log.info("Response (ldName=C1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        assertNotNull(asdu.data());

        //log.info("All data values under C1:");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataEntry entry = asdu.data().get(i);
        //    log.info("  [{}] {} = {}", i, entry.reference().get(), entry.value());
        //}

        assertEquals(3, asdu.data().size());
    }

    @Test
    @DisplayName("by lnReference for MMXU1 returns structured data values")
    void byLnReferenceMmXu1() throws Exception {
        associate();

        CmsApdu response = client.getAllDataValuesByLn("C1/MMXU1");
        //log.info("Response (lnReference=C1/MMXU1): {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        assertNotNull(asdu.data());
        assertEquals(2, asdu.data().size());

        //log.info("Data values for C1/MMXU1:");
        //for (int i = 0; i < asdu.data().size(); i++) {
        //    CmsDataEntry entry = asdu.data().get(i);
        //    log.info("  [{}] {} = {}", i, entry.reference().get(), entry.value());
        //}

        assertEquals("Volts.sVC.offset", asdu.data().get(0).reference().get());
        assertEquals("Volts.sVC.scaleFactor", asdu.data().get(1).reference().get());
    }
}
