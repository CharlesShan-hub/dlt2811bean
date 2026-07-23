// Auto-generated. ASN.1 type: SetDataValuesRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetDataValuesRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetDataValuesRequestPDUData> value;
    public CmsSetDataValuesRequestPDUData() {}
    public CmsSetDataValuesRequestPDUData(java.util.List<CmsAnonymousSetDataValuesRequestPDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetDataValuesRequestPDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetDataValuesRequestPDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetDataValuesRequestPDUData", enc, data);
            CmsSetDataValuesRequestPDUData r = new CmsSetDataValuesRequestPDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetDataValuesRequestPDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
