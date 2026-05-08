package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataSetValues Loopback Test")
class GetDataSetValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid dataset reference returns Response+ with values (§8.5.1.2a)")
    void validDataSet() throws Exception {
        associate();

        CmsApdu response = client.getDataSetValues("C1/LLN0.Positions");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataSetValues asdu = (CmsGetDataSetValues) response.getAsdu();
        assertNotNull(asdu.value());
    }

    @Test
    @DisplayName("unknown dataset returns Response- (§8.5.1.2b)")
    void unknownDataSet() throws Exception {
        associate();

        CmsApdu response = client.getDataSetValues("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty dataset reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getDataSetValues("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
