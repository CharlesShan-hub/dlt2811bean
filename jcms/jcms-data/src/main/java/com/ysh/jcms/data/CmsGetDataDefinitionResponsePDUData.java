// Auto-generated. ASN.1 type: GetDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetDataDefinitionResponsePDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDefinitionResponsePDUData> value;
    public CmsGetDataDefinitionResponsePDUData() {}
    public CmsGetDataDefinitionResponsePDUData(java.util.List<CmsAnonymousGetDataDefinitionResponsePDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDefinitionResponsePDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDefinitionResponsePDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataDefinitionResponsePDUData", enc, data);
            CmsGetDataDefinitionResponsePDUData r = new CmsGetDataDefinitionResponsePDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetDataDefinitionResponsePDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
