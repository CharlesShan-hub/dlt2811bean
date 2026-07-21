// Auto-generated. ASN.1 type: SetLCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetLCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetLCBValuesErrorPDUResult> value;
    public CmsSetLCBValuesErrorPDUResult() {}
    public CmsSetLCBValuesErrorPDUResult(java.util.List<CmsAnonymousSetLCBValuesErrorPDUResult> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetLCBValuesErrorPDUResult", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetLCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetLCBValuesErrorPDUResult", enc, data);
            CmsSetLCBValuesErrorPDUResult r = new CmsSetLCBValuesErrorPDUResult();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetLCBValuesErrorPDUResult>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
