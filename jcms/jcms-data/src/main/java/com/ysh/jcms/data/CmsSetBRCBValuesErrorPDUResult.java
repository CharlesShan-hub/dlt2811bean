// Auto-generated. ASN.1 type: SetBRCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetBRCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetBRCBValuesErrorPDUResult> value;
    public CmsSetBRCBValuesErrorPDUResult() {}
    public CmsSetBRCBValuesErrorPDUResult(java.util.List<CmsAnonymousSetBRCBValuesErrorPDUResult> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetBRCBValuesErrorPDUResult", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetBRCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetBRCBValuesErrorPDUResult", enc, data);
            CmsSetBRCBValuesErrorPDUResult r = new CmsSetBRCBValuesErrorPDUResult();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetBRCBValuesErrorPDUResult>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
