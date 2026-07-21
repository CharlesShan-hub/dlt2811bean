// Auto-generated. ASN.1 type: GetMSVCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetMSVCBValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetMSVCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetMSVCBValuesErrorPDU() {}
    public CmsGetMSVCBValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetMSVCBValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetMSVCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetMSVCBValuesErrorPDU", enc, data);
            CmsGetMSVCBValuesErrorPDU r = new CmsGetMSVCBValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
