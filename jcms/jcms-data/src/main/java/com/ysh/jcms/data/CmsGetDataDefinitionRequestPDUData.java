// Auto-generated. ASN.1 type: GetDataDefinitionRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetDataDefinitionRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDefinitionRequestPDUData> value;
    public CmsGetDataDefinitionRequestPDUData() {}
    public CmsGetDataDefinitionRequestPDUData(java.util.List<CmsAnonymousGetDataDefinitionRequestPDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDefinitionRequestPDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDefinitionRequestPDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataDefinitionRequestPDUData", enc, data);
            CmsGetDataDefinitionRequestPDUData r = new CmsGetDataDefinitionRequestPDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetDataDefinitionRequestPDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
