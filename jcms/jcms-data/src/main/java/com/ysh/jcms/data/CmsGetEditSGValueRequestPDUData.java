// Auto-generated. ASN.1 type: GetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsGetEditSGValueRequestPDUData extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetEditSGValueRequestPDUData> value;
    public CmsGetEditSGValueRequestPDUData() {}
    public CmsGetEditSGValueRequestPDUData(java.util.List<CmsAnonymousGetEditSGValueRequestPDUData> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetEditSGValueRequestPDUData", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetEditSGValueRequestPDUData decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetEditSGValueRequestPDUData", enc, data);
            CmsGetEditSGValueRequestPDUData r = new CmsGetEditSGValueRequestPDUData();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousGetEditSGValueRequestPDUData>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
