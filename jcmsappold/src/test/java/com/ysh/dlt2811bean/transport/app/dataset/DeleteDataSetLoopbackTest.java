package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.service.svc.dataset.CmsDeleteDataSet;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeleteDataSet Loopback Test")
class DeleteDataSetLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("create then delete dataset returns Response+")
    void createThenDelete() throws Exception {
        associate();

        CmsCreateDataSet createAsdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference("C1/LLN0.TempDs")
                .addMemberData("C1/CSWI1.Pos", "ST");
        client.send(createAsdu);

        CmsDeleteDataSet deleteAsdu = new CmsDeleteDataSet(MessageType.REQUEST).datasetReference("C1/LLN0.TempDs");
        CmsApdu response = client.send(deleteAsdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("unknown dataset returns Response-")
    void unknownDataSet() throws Exception {
        associate();

        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST).datasetReference("C1/LLN0.Unknown");
        CmsApdu response = client.send(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void emptyRef() throws Exception {
        associate();

        CmsDeleteDataSet asdu = new CmsDeleteDataSet(MessageType.REQUEST).datasetReference("");
        CmsApdu response = client.send(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
