package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateDataSet Loopback Test")
class CreateDataSetLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("create new data set returns Response+")
    void createDataSet() throws Exception {
        associate();

        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference("C1/LLN0.TestDs")
                .addMemberData("C1/CSWI1.Pos", "ST");
        CmsApdu response = client.send(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference("")
                .addMemberData("C1/CSWI1.Pos", "ST");
        CmsApdu response = client.send(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
