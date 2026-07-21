// Auto-generated. ASN.1 type: GetAllDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetAllDataDefinitionResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetAllDataDefinitionResponsePDUData> value;
    public CmsGetAllDataDefinitionResponsePDUData() {}
    public CmsGetAllDataDefinitionResponsePDUData(java.util.List<CmsAnonymousGetAllDataDefinitionResponsePDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataDefinitionResponsePDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataDefinitionResponsePDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetAllDataDefinitionResponsePDUData", enc, data);
            CmsGetAllDataDefinitionResponsePDUData r = new CmsGetAllDataDefinitionResponsePDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetAllDataDefinitionResponsePDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
