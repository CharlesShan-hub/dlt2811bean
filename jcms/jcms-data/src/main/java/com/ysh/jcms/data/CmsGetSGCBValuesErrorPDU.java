// Auto-generated. ASN.1 type: GetSGCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetSGCBValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetSGCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetSGCBValuesErrorPDU() {}
    public CmsGetSGCBValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetSGCBValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetSGCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetSGCBValuesErrorPDU", enc, data);
            CmsGetSGCBValuesErrorPDU r = new CmsGetSGCBValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
