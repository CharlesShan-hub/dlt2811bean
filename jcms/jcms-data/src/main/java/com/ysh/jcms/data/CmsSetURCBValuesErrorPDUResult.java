// Auto-generated. ASN.1 type: SetURCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetURCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetURCBValuesErrorPDUResult> value;
    public CmsSetURCBValuesErrorPDUResult() {}
    public CmsSetURCBValuesErrorPDUResult(java.util.List<CmsAnonymousSetURCBValuesErrorPDUResult> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetURCBValuesErrorPDUResult", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetURCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetURCBValuesErrorPDUResult", enc, data);
            CmsSetURCBValuesErrorPDUResult r = new CmsSetURCBValuesErrorPDUResult();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetURCBValuesErrorPDUResult>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
