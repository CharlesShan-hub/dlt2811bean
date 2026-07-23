// Auto-generated. ASN.1 type: SetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetEditSGValueRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetEditSGValueRequestPDUData> value;
    public CmsSetEditSGValueRequestPDUData() {}
    public CmsSetEditSGValueRequestPDUData(java.util.List<CmsAnonymousSetEditSGValueRequestPDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetEditSGValueRequestPDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetEditSGValueRequestPDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetEditSGValueRequestPDUData", enc, data);
            CmsSetEditSGValueRequestPDUData r = new CmsSetEditSGValueRequestPDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetEditSGValueRequestPDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
