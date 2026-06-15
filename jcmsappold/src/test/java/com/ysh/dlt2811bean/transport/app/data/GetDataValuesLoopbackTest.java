package com.ysh.dlt2811bean.transport.app.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataValues Loopback Test")
class GetDataValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("get single DAI value by reference")
    void singleValue() throws Exception {
        associate();

        CmsGetDataValues asduReq = new CmsGetDataValues(MessageType.REQUEST);
        asduReq.data.add(new CmsGetDataValuesEntry().reference("C1/LPHD1.Proxy.stVal"));
        CmsApdu response = client.send(asduReq);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetDataValues asdu = (CmsGetDataValues) response.getAsdu();
        CmsStructure values = asdu.value();
        assertNotNull(values);
        assertEquals(1, values.size());

        CmsData<?> data = values.get(0);
        log.info("Value: {}", data);
        assertEquals("Data[16]=(CmsVisibleString) false", data.toString());
    }

    @Test
    @DisplayName("get multiple DAI values in one request")
    void multipleValues() throws Exception {
        associate();

        CmsGetDataValues asduReq = new CmsGetDataValues(MessageType.REQUEST);
        asduReq.data.add(new CmsGetDataValuesEntry().reference("C1/LPHD1.Proxy.stVal"));
        asduReq.data.add(new CmsGetDataValuesEntry().reference("C1/MMXU1.Volts.sVC.offset"));
        asduReq.data.add(new CmsGetDataValuesEntry().reference("C1/MMXU1.Volts.sVC.scaleFactor"));
        CmsApdu response = client.send(asduReq);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetDataValues asdu = (CmsGetDataValues) response.getAsdu();
        CmsStructure values = asdu.value();
        assertNotNull(values);
        assertEquals(3, values.size());

        for (int i = 0; i < values.size(); i++) {
            log.info("  [{}] {}", i, values.get(i));
        }
    }

    @Test
    @DisplayName("non-existent reference returns error in response value")
    void nonExistentReference() throws Exception {
        associate();

        CmsGetDataValues asduReq = new CmsGetDataValues(MessageType.REQUEST);
        asduReq.data.add(new CmsGetDataValuesEntry().reference("C1/FAKE.DO.stVal"));
        CmsApdu response = client.send(asduReq);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetDataValues asdu = (CmsGetDataValues) response.getAsdu();
        CmsStructure values = asdu.value();
        assertNotNull(values);
        assertEquals(1, values.size());

        CmsData<?> data = values.get(0);
        log.info("Error value: {}", data);
        assertEquals("Data[0]=(CmsServiceError) 1", data.toString());
    }
}
