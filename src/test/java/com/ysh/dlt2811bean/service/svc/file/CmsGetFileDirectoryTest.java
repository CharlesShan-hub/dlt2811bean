package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.compound.CmsFileEntry;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetFileDirectory")
class CmsGetFileDirectoryTest {

    @Test
    @DisplayName("REQUEST: encode and decode round-trip via APDU")
    void requestRoundTrip() throws Exception {
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.REQUEST)
            .pathName("/var/log")
            .fileAfter("log2.txt")
            .reqId(1);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileDirectory result = (CmsGetFileDirectory) decoded.getAsdu();
        assertEquals(1, result.reqId().get());
        assertEquals("/var/log", result.pathName().get());
        assertEquals("log2.txt", result.fileAfter().get());
    }

    @Test
    @DisplayName("RESPONSE_POSITIVE: encode and decode round-trip via APDU")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.RESPONSE_POSITIVE)
            .reqId(2);

        CmsFileEntry entry = new CmsFileEntry();
        entry.fileName.set("log1.txt");
        entry.fileSize.set(1024L);
        asdu.fileEntry().add(entry);
        asdu.moreFollows().set(true);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileDirectory result = (CmsGetFileDirectory) decoded.getAsdu();
        assertEquals(2, result.reqId().get());
        assertEquals(1, result.fileEntry().size());
        assertEquals("log1.txt", result.fileEntry().get(0).fileName.get());
        assertEquals(1024L, result.fileEntry().get(0).fileSize.get());
        assertTrue(result.moreFollows().get());
    }

    @Test
    @DisplayName("RESPONSE_NEGATIVE: encode and decode round-trip via APDU")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.RESPONSE_NEGATIVE)
            .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE)
            .reqId(3);

        CmsApdu apdu = new CmsApdu(asdu);

        PerOutputStream pos = new PerOutputStream();
        apdu.encode(pos);

        CmsApdu decoded = new CmsApdu().decode(new PerInputStream(pos.toByteArray()));

        CmsGetFileDirectory result = (CmsGetFileDirectory) decoded.getAsdu();
        assertEquals(3, result.reqId().get());
        assertEquals(CmsServiceError.INSTANCE_NOT_AVAILABLE, result.serviceError().get());
    }

    @Test
    @DisplayName("getServiceCode returns GET_FILE_DIRECTORY")
    void serviceCode() {
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.REQUEST);
        assertEquals(ServiceName.GET_FILE_DIRECTORY, asdu.getServiceName());
    }
}
