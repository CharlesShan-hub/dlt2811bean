// Auto-generated. ASN.1 type: GetAllDataValuesResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetAllDataValuesResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetAllDataValuesResponsePDUData> value;
    public CmsGetAllDataValuesResponsePDUData() {}
    public CmsGetAllDataValuesResponsePDUData(java.util.List<CmsAnonymousGetAllDataValuesResponsePDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataValuesResponsePDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataValuesResponsePDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetAllDataValuesResponsePDUData", enc, data);
            CmsGetAllDataValuesResponsePDUData r = new CmsGetAllDataValuesResponsePDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetAllDataValuesResponsePDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
