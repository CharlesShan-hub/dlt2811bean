package com.ysh.dlt2811bean.transport.app.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetDataValues Loopback Test")
class GetDataValuesLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("get single DAI value by reference")
    void singleValue() throws Exception {
        associate();

        CmsApdu response = client.getDataValues("C1/LPHD1.Proxy.stVal");
        log.info("Response: {}", response);

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

        CmsApdu response = client.getDataValues(
                "C1/LPHD1.Proxy.stVal",
                "C1/MMXU1.Volts.sVC.offset",
                "C1/MMXU1.Volts.sVC.scaleFactor");
        log.info("Response: {}", response);

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

        CmsApdu response = client.getDataValues("C1/FAKE.DO.stVal");
        log.info("Response: {}", response);

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

    @Test
    @DisplayName("request with fc filter")
    void withFcFilter() throws Exception {
        associate();

        CmsApdu response = client.getDataValuesWithFc("ST", "C1/LPHD1.Proxy.stVal");
        log.info("Response: {}", response);

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());

        CmsGetDataValues asdu = (CmsGetDataValues) response.getAsdu();
        CmsStructure values = asdu.value();
        assertNotNull(values);
        assertEquals(1, values.size());
    }
}
