package com.ysh.jcms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for auto-generated ASN.1 data types (Jackson POJOs).
 *
 * These tests cover construction, field access, default values,
 * and JSON round-trip serialization. The encode/decode methods
 * require the native asn1.dll library and are not tested here.
 */
public class DataTypesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ================================================================
    // Newtype (wrapper / delegate) tests
    // ================================================================

    @Test
    public void testCmsBoolean_defaultConstructor() {
        CmsBoolean b = new CmsBoolean();
        assertEquals(0, b.value);
    }

    @Test
    public void testCmsBoolean_valueConstructor() {
        CmsBoolean b = new CmsBoolean(1);
        assertEquals(1, b.value);
    }

    @Test
    public void testCmsBoolean_jsonRoundTrip() throws Exception {
        CmsBoolean b = new CmsBoolean(1);
        String json = MAPPER.writeValueAsString(b);
        assertTrue(json.contains("1"));

        CmsBoolean deserialized = MAPPER.readValue(json, CmsBoolean.class);
        assertEquals(1, deserialized.value);
    }

    @Test
    public void testCmsInt8U_defaultConstructor() {
        CmsInt8U v = new CmsInt8U();
        assertEquals(0, v.value);
    }

    @Test
    public void testCmsInt8U_jsonRoundTrip() throws Exception {
        CmsInt8U v = new CmsInt8U(128);
        String json = MAPPER.writeValueAsString(v);
        CmsInt8U d = MAPPER.readValue(json, CmsInt8U.class);
        assertEquals(128, d.value);
    }

    @Test
    public void testCmsUtcTime_default() {
        CmsUtcTime t = new CmsUtcTime();
        assertNull(t.value);
    }

    // ================================================================
    // Struct (SEQUENCE) tests
    // ================================================================

    @Test
    public void testCmsSGCB_defaultValues() {
        CmsSGCB sgcb = new CmsSGCB();
        assertEquals(0, sgcb.num_of_sg);
        assertEquals(0, sgcb.act_sg);
        assertEquals(0, sgcb.edit_sg);
        assertNull(sgcb.t_act_edt);
        assertNull(sgcb.resv_tms);
    }

    @Test
    public void testCmsSGCB_jsonRoundTrip() throws Exception {
        CmsSGCB sgcb = new CmsSGCB();
        sgcb.num_of_sg = 2;
        sgcb.act_sg = 1;
        sgcb.edit_sg = 2;

        String json = MAPPER.writeValueAsString(sgcb);
        // Should include only non-null fields
        assertTrue(json.contains("\"num_of_sg\":2"));
        assertTrue(json.contains("\"act_sg\":1"));
        assertTrue(json.contains("\"edit_sg\":2"));
        // null optional fields should be excluded (@JsonInclude NON_NULL)
        assertFalse(json.contains("resv_tms"));
        assertFalse(json.contains("t_act_edt"));

        CmsSGCB deserialized = MAPPER.readValue(json, CmsSGCB.class);
        assertEquals(2, deserialized.num_of_sg);
        assertEquals(1, deserialized.act_sg);
        assertEquals(2, deserialized.edit_sg);
        assertNull(deserialized.resv_tms);
    }

    @Test
    public void testCmsApdu_defaultValues() {
        CmsApdu apdu = new CmsApdu();
        assertNull(apdu.apch);
        assertNull(apdu.asdu);
    }

    @Test
    public void testCmsApdu_jsonRoundTrip() throws Exception {
        CmsApdu apdu = new CmsApdu();
        apdu.apch = new CmsApch();
        apdu.apch.sc = 10;
        apdu.asdu = new byte[]{0x01, 0x02, 0x03};

        String json = MAPPER.writeValueAsString(apdu);
        assertTrue(json.contains("sc"));
        assertTrue(json.contains("10"));
        assertTrue(json.contains("AQID"));

        CmsApdu deserialized = MAPPER.readValue(json, CmsApdu.class);
        assertEquals(10, deserialized.apch.sc);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, deserialized.asdu);
    }

    @Test
    public void testCmsAssociateRequestPDU_withOptionals() throws Exception {
        CmsAssociateRequestPDU req = new CmsAssociateRequestPDU();
        req.server_access_point_reference = "C_B5041X/S1";
        req.authentication_parameter = new CmsAssociateRequestPDUAuthenticationParameter();
        req.authentication_parameter.signature_certificate = new byte[]{0x0a, 0x0b};
        req.authentication_parameter.signed_time = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        req.authentication_parameter.signed_value = new byte[]{0x10};

        String json = MAPPER.writeValueAsString(req);
        assertTrue(json.contains("C_B5041X/S1"));

        CmsAssociateRequestPDU deserialized = MAPPER.readValue(json, CmsAssociateRequestPDU.class);
        assertEquals("C_B5041X/S1", deserialized.server_access_point_reference);
        assertNotNull(deserialized.authentication_parameter);
        assertArrayEquals(new byte[]{0x0a, 0x0b}, deserialized.authentication_parameter.signature_certificate);
    }

    @Test
    public void testCmsAssociateRequestPDU_emptyDefaults() {
        CmsAssociateRequestPDU req = new CmsAssociateRequestPDU();
        // All fields are optional — defaults should all be null
        assertNull(req.server_access_point_reference);
        assertNull(req.authentication_parameter);
    }

    // ================================================================
    // List (SEQUENCE OF) tests
    // ================================================================

    @Test
    public void testCmsGetAllCBValuesResponsePDU_listField() throws Exception {
        CmsGetAllCBValuesResponsePDU resp = new CmsGetAllCBValuesResponsePDU();
        assertNull(resp.cb_value);
        assertFalse(resp.more_follows); // DEFAULT TRUE — default is false in Java

        CmsAnonymousGetAllCBValuesResponsePDUCbValue cb = new CmsAnonymousGetAllCBValuesResponsePDUCbValue();
        cb.reference = "PROT/PTOC1";
        resp.cb_value = Arrays.asList(cb);

        String json = MAPPER.writeValueAsString(resp);
        assertTrue(json.contains("PROT/PTOC1"));
        assertTrue(json.contains("cb_value"));

        CmsGetAllCBValuesResponsePDU deserialized = MAPPER.readValue(json, CmsGetAllCBValuesResponsePDU.class);
        assertEquals(1, deserialized.cb_value.size());
        assertEquals("PROT/PTOC1", deserialized.cb_value.get(0).reference);
    }

    // ================================================================
    // Choice tests
    // ================================================================

    @Test
    public void testCmsGetAllCBValuesRequestPDUReference_ldName() throws Exception {
        CmsGetAllCBValuesRequestPDUReference ref = new CmsGetAllCBValuesRequestPDUReference();
        ref._choice = "ldName";
        ref.ldName = "C_B5041X";

        String json = MAPPER.writeValueAsString(ref);
        assertTrue(json.contains("_choice"));
        assertTrue(json.contains("ldName"));
        assertTrue(json.contains("C_B5041X"));

        CmsGetAllCBValuesRequestPDUReference deserialized =
            MAPPER.readValue(json, CmsGetAllCBValuesRequestPDUReference.class);
        assertEquals("ldName", deserialized._choice);
        assertEquals("C_B5041X", deserialized.ldName);
    }

    @Test
    public void testCmsGetAllCBValuesRequestPDUReference_lnReference() throws Exception {
        CmsGetAllCBValuesRequestPDUReference ref = new CmsGetAllCBValuesRequestPDUReference();
        ref._choice = "lnReference";
        ref.lnReference = "C_B5041X/PTOC1";

        String json = MAPPER.writeValueAsString(ref);
        assertTrue(json.contains("lnReference"));

        CmsGetAllCBValuesRequestPDUReference deserialized =
            MAPPER.readValue(json, CmsGetAllCBValuesRequestPDUReference.class);
        assertEquals("lnReference", deserialized._choice);
        assertEquals("C_B5041X/PTOC1", deserialized.lnReference);
    }

    @Test
    public void testCmsData_choiceInt32() throws Exception {
        CmsData data = new CmsData();
        data._choice = "int32";
        data.int32 = 42;

        String json = MAPPER.writeValueAsString(data);
        assertTrue(json.contains("int32"));
        assertTrue(json.contains("42"));

        CmsData deserialized = MAPPER.readValue(json, CmsData.class);
        assertEquals("int32", deserialized._choice);
        assertEquals(42, deserialized.int32);
    }

    @Test
    public void testCmsData_choiceVisibleString() throws Exception {
        CmsData data = new CmsData();
        data._choice = "visible_string";
        data.visible_string = "hello";

        String json = MAPPER.writeValueAsString(data);
        assertTrue(json.contains("visible_string"));
        assertTrue(json.contains("hello"));

        CmsData deserialized = MAPPER.readValue(json, CmsData.class);
        assertEquals("visible_string", deserialized._choice);
        assertEquals("hello", deserialized.visible_string);
    }

    @Test
    public void testCmsData_choiceOctetString() throws Exception {
        CmsData data = new CmsData();
        data._choice = "octet_string";
        data.octet_string = new byte[]{0x00, (byte)0xFF, (byte)0xAB};

        String json = MAPPER.writeValueAsString(data);
        assertTrue(json.contains("octet_string"));

        CmsData deserialized = MAPPER.readValue(json, CmsData.class);
        assertEquals("octet_string", deserialized._choice);
        assertArrayEquals(new byte[]{0x00, (byte)0xFF, (byte)0xAB}, deserialized.octet_string);
    }

    @Test
    public void testCmsData_choiceBool() throws Exception {
        CmsData data = new CmsData();
        data._choice = "boolean";
        data._boolean = true;

        String json = MAPPER.writeValueAsString(data);
        CmsData deserialized = MAPPER.readValue(json, CmsData.class);
        assertEquals("boolean", deserialized._choice);
        assertTrue(deserialized._boolean);
    }

    // ================================================================
    // ServiceError (enum-like delegate)
    // ================================================================

    @Test
    public void testCmsServiceError_jsonRoundTrip() throws Exception {
        CmsServiceError err = new CmsServiceError(3);
        String json = MAPPER.writeValueAsString(err);
        CmsServiceError deserialized = MAPPER.readValue(json, CmsServiceError.class);
        assertEquals(3, deserialized.value);
    }

    // ================================================================
    // Byte-array types
    // ================================================================

    @Test
    public void testCmsUtcTime_jsonRoundTrip() throws Exception {
        byte[] raw = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        CmsUtcTime t = new CmsUtcTime();
        t.value = raw;

        String json = MAPPER.writeValueAsString(t);
        assertTrue(json.contains("AQIDBAUGBwg"));

        CmsUtcTime deserialized = MAPPER.readValue(json, CmsUtcTime.class);
        assertArrayEquals(raw, deserialized.value);
    }

    // ================================================================
    // DEFAULT_ENCODING constant
    // ================================================================

    @Test
    public void testDefaultEncoding() {
        assertEquals("per", CmsBoolean.DEFAULT_ENCODING);
        assertEquals("per", CmsSGCB.DEFAULT_ENCODING);
        assertEquals("per", CmsData.DEFAULT_ENCODING);
    }
}
