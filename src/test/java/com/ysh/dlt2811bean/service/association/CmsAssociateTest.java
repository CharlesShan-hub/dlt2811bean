package com.ysh.dlt2811bean.service.association;

import org.junit.jupiter.api.DisplayName;

@DisplayName("CmsAssociate (SC=1)")
class CmsAssociateTest {

//    @Test
//    @DisplayName("encode+decode round-trip")
//    void roundTrip() throws PerDecodeException {
//        Cms01 req = new Cms01();
//        req.setReqId(42);
//        req.setProtocolVersion(1);
//        req.setApduSize(65535);
//        req.setAsduSize(65531);
//        req.setServerName("MyServer");
//
//        byte[] frame = req.encode();
//
//        Cms01 decoded = new Cms01();
//        decoded.decode(frame);
//
//        assertEquals(42, decoded.getReqId());
//        assertEquals(1, decoded.getProtocolVersion());
//        assertEquals(65535, decoded.getApduSize());
//        assertEquals(65531, decoded.getAsduSize());
//        assertEquals("MyServer", decoded.getServerName());
//    }
//
//    @Test
//    @DisplayName("APCH header: PI=0x01, SC=0x01")
//    void header() {
//        Cms01 req = new Cms01();
//        req.setReqId(0);
//        req.setProtocolVersion(1);
//        req.setApduSize(1024);
//        req.setAsduSize(512);
//        req.setServerName("");
//
//        byte[] frame = req.encode();
//
//        assertEquals(0x01, frame[0] & 0xFF);  // PI
//        assertEquals(0x01, frame[1] & 0xFF);  // SC
//    }
//
//    @Test
//    @DisplayName("response flag in header")
//    void responseFlag() throws PerDecodeException {
//        Cms01 req = new Cms01();
//        req.setResponse(true);
//        req.setReqId(1);
//        req.setProtocolVersion(1);
//        req.setApduSize(1024);
//        req.setAsduSize(512);
//        req.setServerName("");
//
//        byte[] frame = req.encode();
//        assertTrue((frame[2] & 0x80) != 0);  // Resp bit
//
//        Cms01 decoded = new Cms01();
//        decoded.decode(frame);
//        assertTrue(decoded.isResponse());
//    }
//
//    @Test
//    @DisplayName("error flag in header")
//    void errorFlag() throws PerDecodeException {
//        Cms01 req = new Cms01();
//        req.setError(true);
//        req.setReqId(1);
//        req.setProtocolVersion(1);
//        req.setApduSize(1024);
//        req.setAsduSize(512);
//        req.setServerName("");
//
//        byte[] frame = req.encode();
//        assertTrue((frame[2] & 0x40) != 0);  // Err bit
//
//        Cms01 decoded = new Cms01();
//        decoded.decode(frame);
//        assertTrue(decoded.isError());
//    }
//
//    @Test
//    @DisplayName("decode invalid PI throws")
//    void invalidPi() {
//        Cms01 req = new Cms01();
//        req.setReqId(1);
//        req.setProtocolVersion(1);
//        req.setApduSize(1024);
//        req.setAsduSize(512);
//        req.setServerName("");
//        byte[] frame = req.encode();
//        frame[0] = 0x02;  // corrupt PI
//
//        Cms01 decoded = new Cms01();
//        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
//    }
//
//    @Test
//    @DisplayName("decode service code mismatch throws")
//    void serviceCodeMismatch() {
//        byte[] frame = {0x01, 0x03, 0x00, 0x00, 0x00};  // SC=3 but Cms01 expects SC=1
//
//        Cms01 decoded = new Cms01();
//        assertThrows(PerDecodeException.class, () -> decoded.decode(frame));
//    }
//
//    @Test
//    @DisplayName("empty serverName round-trip")
//    void emptyServerName() throws PerDecodeException {
//        Cms01 req = new Cms01();
//        req.setReqId(1);
//        req.setProtocolVersion(1);
//        req.setApduSize(4096);
//        req.setAsduSize(2048);
//        req.setServerName("");
//
//        byte[] frame = req.encode();
//
//        Cms01 decoded = new Cms01();
//        decoded.decode(frame);
//        assertEquals("", decoded.getServerName());
//    }
//
//    @Test
//    @DisplayName("FL matches ASDU length")
//    void frameLength() {
//        Cms01 req = new Cms01();
//        req.setReqId(0);
//        req.setProtocolVersion(1);
//        req.setApduSize(1024);
//        req.setAsduSize(512);
//        req.setServerName("test");
//
//        byte[] frame = req.encode();
//        int fl = ((frame[3] & 0xFF) << 8) | (frame[4] & 0xFF);
//        assertEquals(frame.length - 5, fl);
//    }
}
