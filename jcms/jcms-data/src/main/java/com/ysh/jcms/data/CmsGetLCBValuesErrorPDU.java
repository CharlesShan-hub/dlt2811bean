// Auto-generated. ASN.1 type: GetLCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLCBValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetLCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetLCBValuesErrorPDU() {}
    public CmsGetLCBValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetLCBValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLCBValuesErrorPDU", enc, data);
            CmsGetLCBValuesErrorPDU r = new CmsGetLCBValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
