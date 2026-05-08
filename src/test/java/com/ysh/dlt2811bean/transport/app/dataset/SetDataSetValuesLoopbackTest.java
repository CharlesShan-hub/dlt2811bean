package com.ysh.dlt2811bean.transport.app.dataset;

import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetDataSetValues Loopback Test")
class SetDataSetValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("set valid dataset values returns Response+ (§8.5.2.2d)")
    void validDataSet() throws Exception {
        associate();

        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference("C1/LLN0.Positions")
                .addMemberValue(new CmsVisibleString("true").max(255));

        CmsApdu response = client.setDataSetValues(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("unknown dataset returns Response-")
    void unknownDataSet() throws Exception {
        associate();

        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference("C1/LLN0.Unknown")
                .addMemberValue(new CmsVisibleString("true").max(255));

        CmsApdu response = client.setDataSetValues(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
