// Auto-generated. ASN.1 type: GetBRCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetBRCBValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetBRCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetBRCBValuesErrorPDU() {}
    public CmsGetBRCBValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetBRCBValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetBRCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetBRCBValuesErrorPDU", enc, data);
            CmsGetBRCBValuesErrorPDU r = new CmsGetBRCBValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
