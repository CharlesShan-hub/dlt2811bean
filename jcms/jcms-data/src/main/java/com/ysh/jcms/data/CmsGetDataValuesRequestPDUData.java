// Auto-generated. ASN.1 type: GetDataValuesRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetDataValuesRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataValuesRequestPDUData> value;
    public CmsGetDataValuesRequestPDUData() {}
    public CmsGetDataValuesRequestPDUData(java.util.List<CmsAnonymousGetDataValuesRequestPDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataValuesRequestPDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataValuesRequestPDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataValuesRequestPDUData", enc, data);
            CmsGetDataValuesRequestPDUData r = new CmsGetDataValuesRequestPDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetDataValuesRequestPDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
