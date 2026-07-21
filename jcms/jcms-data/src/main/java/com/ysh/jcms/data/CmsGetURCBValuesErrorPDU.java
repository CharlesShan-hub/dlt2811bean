// Auto-generated. ASN.1 type: GetURCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetURCBValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetURCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetURCBValuesErrorPDU() {}
    public CmsGetURCBValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetURCBValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetURCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetURCBValuesErrorPDU", enc, data);
            CmsGetURCBValuesErrorPDU r = new CmsGetURCBValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
