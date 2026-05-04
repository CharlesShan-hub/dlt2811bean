package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOperate")
class CmsOperateTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsOperate asdu = new CmsOperate(MessageType.REQUEST)
            .reference("IED1.AP1.LD1.LN1.DO1")
            .ctlVal(new CmsInt32(100))
            .ctlNum(1)
            .test(false)
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsOperate result = (CmsOperate) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.reference().get());
        assertEquals(100, ((CmsInt32) ((com.ysh.dlt2811bean.datatypes.data.CmsData) result.ctlVal()).get()).get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsOperate asdu = new CmsOperate(MessageType.RESPONSE_NEGATIVE)
            .reference("IED1.AP1.LD1.LN1.DO1")
            .ctlVal(new CmsInt32(100))
            .ctlNum(1)
            .test(false)
            .addCause(CmsAddCause.OBJECT_NOT_SELECTED)
            .reqId(2);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsOperate result = (CmsOperate) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(CmsAddCause.OBJECT_NOT_SELECTED, result.addCause().get());
    }

    @Test
    @DisplayName("getServiceCode returns OPERATE")
    void serviceCode() {
        CmsOperate asdu = new CmsOperate(MessageType.REQUEST);
        assertEquals(ServiceName.OPERATE, asdu.getServiceName());
    }

    @Test
    @DisplayName("fromFlags constructor: REQUEST")
    void fromFlagsRequest() {
        CmsOperate asdu = new CmsOperate(false, false);
        assertEquals(MessageType.REQUEST, asdu.messageType());
    }
}
