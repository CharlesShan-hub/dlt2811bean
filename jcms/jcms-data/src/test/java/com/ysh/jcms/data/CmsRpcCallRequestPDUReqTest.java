// Auto-generated. Tests for CmsRpcCallRequestPDUReq

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsRpcCallRequestPDUReqTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoicereqData() throws Exception {
        CmsRpcCallRequestPDUReq obj = new CmsRpcCallRequestPDUReq();
        obj._choice = "reqData";
        obj.reqData = new CmsData();
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallRequestPDUReq d = MAPPER.readValue(json, CmsRpcCallRequestPDUReq.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicecallID() throws Exception {
        CmsRpcCallRequestPDUReq obj = new CmsRpcCallRequestPDUReq();
        obj._choice = "callID";
        obj.callID = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsRpcCallRequestPDUReq d = MAPPER.readValue(json, CmsRpcCallRequestPDUReq.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsRpcCallRequestPDUReq obj = new CmsRpcCallRequestPDUReq();
        obj._choice = "reqData";
        obj.reqData = new CmsData();
        byte[] data = obj.encode("uper");
        CmsRpcCallRequestPDUReq d = CmsRpcCallRequestPDUReq.decode("uper", data);
        assertEquals(obj, d);
    }
}
