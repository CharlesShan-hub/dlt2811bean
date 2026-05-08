package com.ysh.dlt2811bean.transport.app.setting;

import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SetEditSGValue Loopback Test")
class SetEditSGValueLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("set valid reference returns Response+")
    void validRef() throws Exception {
        associate();

        CmsSetEditSGValue asdu = new CmsSetEditSGValue(MessageType.REQUEST)
                .addData("C1/LPHD1.Proxy.stVal", new CmsVisibleString("false").max(255));

        CmsApdu response = client.setEditSGValue(asdu);

        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
    }

    @Test
    @DisplayName("empty reference returns Response-")
    void invalidRef() throws Exception {
        associate();

        CmsSetEditSGValue asdu = new CmsSetEditSGValue(MessageType.REQUEST)
                .addData("", new CmsVisibleString("test").max(255));

        CmsApdu response = client.setEditSGValue(asdu);

        assertEquals(MessageType.RESPONSE_NEGATIVE, response.getMessageType());
    }
}
