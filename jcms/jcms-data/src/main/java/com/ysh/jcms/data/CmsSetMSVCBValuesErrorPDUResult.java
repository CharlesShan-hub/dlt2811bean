// Auto-generated. ASN.1 type: SetMSVCBValuesErrorPDUResult

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

@Data
public class CmsSetMSVCBValuesErrorPDUResult extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetMSVCBValuesErrorPDUResult> value;
    public CmsSetMSVCBValuesErrorPDUResult() {}
    public CmsSetMSVCBValuesErrorPDUResult(java.util.List<CmsAnonymousSetMSVCBValuesErrorPDUResult> value) { this.value = value; }
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetMSVCBValuesErrorPDUResult", enc, MAPPER.writeValueAsString(this.value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetMSVCBValuesErrorPDUResult decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SetMSVCBValuesErrorPDUResult", enc, data);
            CmsSetMSVCBValuesErrorPDUResult r = new CmsSetMSVCBValuesErrorPDUResult();
            r.value = MAPPER.readValue(json.trim(), new com.fasterxml.jackson.core.type.TypeReference<java.util.List<CmsAnonymousSetMSVCBValuesErrorPDUResult>>() {});
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
