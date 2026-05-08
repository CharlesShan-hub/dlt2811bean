package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataSetDirectory Loopback Test")
class GetDataSetDirectoryLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("valid dataset returns directory with member entries")
    void validDataSet() throws Exception {
        associate();

        CmsApdu response = client.getDataSetDirectory("C1/LLN0.Positions");

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        CmsGetDataSetDirectory asdu = (CmsGetDataSetDirectory) response.getAsdu();
        assertTrue(asdu.memberData.size() > 0);
    }

    @Test
    @DisplayName("unknown dataset returns Response-")
    void unknownDataSet() throws Exception {
        associate();

        CmsApdu response = client.getDataSetDirectory("C1/LLN0.Unknown");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsApdu response = client.getDataSetDirectory("");

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
