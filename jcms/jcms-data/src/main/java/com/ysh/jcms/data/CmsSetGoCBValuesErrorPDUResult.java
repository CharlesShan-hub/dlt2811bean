// Auto-generated. ASN.1 type: SetGoCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetGoCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetGoCBValuesErrorPDUResult> value;
    public CmsSetGoCBValuesErrorPDUResult() {}
    public CmsSetGoCBValuesErrorPDUResult(java.util.List<CmsAnonymousSetGoCBValuesErrorPDUResult> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetGoCBValuesErrorPDUResult", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetGoCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetGoCBValuesErrorPDUResult", enc, data);
            CmsSetGoCBValuesErrorPDUResult r = new CmsSetGoCBValuesErrorPDUResult();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetGoCBValuesErrorPDUResult>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
