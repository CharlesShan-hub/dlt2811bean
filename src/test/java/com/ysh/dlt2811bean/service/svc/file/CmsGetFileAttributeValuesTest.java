package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetFileAttributeValues")
class CmsGetFileAttributeValuesTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST)
            .fileName("report.txt")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileAttributeValues result = (CmsGetFileAttributeValues) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("report.txt", result.fileName().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.RESPONSE_POSITIVE)
            .reqId(2);
        asdu.fileEntry().fileName.set("test.txt");
        asdu.fileEntry().fileSize.set(1024L);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileAttributeValues result = (CmsGetFileAttributeValues) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals("test.txt", result.fileEntry().fileName.get());
        assertEquals(1024L, result.fileEntry().fileSize.get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileAttributeValues result = (CmsGetFileAttributeValues) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_FILE_ATTRIBUTEVALUES")
    void serviceCode() {
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST);
        assertEquals(ServiceName.GET_FILE_ATTRIBUTEVALUES, asdu.getServiceName());
    }
}
