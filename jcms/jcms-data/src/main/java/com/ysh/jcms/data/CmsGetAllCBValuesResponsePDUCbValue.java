// Auto-generated. ASN.1 type: GetAllCBValuesResponsePDUCbValue

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetAllCBValuesResponsePDUCbValue extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetAllCBValuesResponsePDUCbValue> value;
    public CmsGetAllCBValuesResponsePDUCbValue() {}
    public CmsGetAllCBValuesResponsePDUCbValue(java.util.List<CmsAnonymousGetAllCBValuesResponsePDUCbValue> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllCBValuesResponsePDUCbValue", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllCBValuesResponsePDUCbValue decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetAllCBValuesResponsePDUCbValue", enc, data);
            CmsGetAllCBValuesResponsePDUCbValue r = new CmsGetAllCBValuesResponsePDUCbValue();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetAllCBValuesResponsePDUCbValue>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
