package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGetFileAttributeValues")
class CmsGetFileAttributeValuesTest implements ServiceNameTest<CmsGetFileAttributeValues> {

    @Override public ServiceName expectedServiceName() { return ServiceName.GET_FILE_ATTRIBUTEVALUES; }
    @Override public CmsGetFileAttributeValues createAsdu() { return new CmsGetFileAttributeValues(MessageType.REQUEST); }

    @Test @DisplayName("REQUEST round-trip")
    void requestRoundTrip() throws Exception {
        CmsGetFileAttributeValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFileAttributeValues(MessageType.REQUEST)
                        .fileName("report.txt").reqId(1));
        assertEquals(1, result.reqId().get());
        assertEquals("report.txt", result.fileName().get());
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsGetFileAttributeValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFileAttributeValues(MessageType.RESPONSE_POSITIVE).reqId(2));
        assertEquals(2, result.reqId().get());
    }

    @Test @DisplayName("RESPONSE_NEGATIVE round-trip")
    void negativeResponseRoundTrip() throws Exception {
        CmsGetFileAttributeValues result = AsduTestUtil.roundTripViaApdu(
                new CmsGetFileAttributeValues(MessageType.RESPONSE_NEGATIVE)
                        .serviceError(CmsServiceError.INSTANCE_NOT_AVAILABLE).reqId(3));
        assertEquals(3, result.reqId().get());
    }
}
