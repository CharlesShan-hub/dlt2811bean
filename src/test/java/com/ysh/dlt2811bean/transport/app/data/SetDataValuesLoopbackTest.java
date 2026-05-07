package com.ysh.dlt2811bean.transport.app.data;

import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetDataValues Loopback Test")
class SetDataValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("set valid reference returns Response+")
    void validReference() throws Exception {
        associate();

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
                .addData("C1/LPHD1.Proxy.stVal", null,
                        new CmsVisibleString("false").max(255));

        CmsApdu response = client.setDataValues(asdu);
        log.info("Response: {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("set invalid reference returns Response- with error")
    void invalidReference() throws Exception {
        associate();

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST)
                .addData("C1/FAKE.DO.stVal", null,
                        new CmsVisibleString("test").max(255));

        CmsApdu response = client.setDataValues(asdu);
        log.info("Response: {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("mixed valid and invalid returns Response-")
    void mixedReferences() throws Exception {
        associate();

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        asdu.addData("C1/LPHD1.Proxy.stVal", null,
                new CmsVisibleString("false").max(255));
        asdu.addData("C1/FAKE.DO.stVal", null,
                new CmsVisibleString("test").max(255));
        asdu.addData("C1/MMXU1.Volts.sVC.offset", null,
                new CmsVisibleString("20").max(255));

        CmsApdu response = client.setDataValues(asdu);
        log.info("Response: {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
