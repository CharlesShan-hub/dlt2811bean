// Auto-generated. ASN.1 type: GetLogStatusValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogStatusValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetLogStatusValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetLogStatusValuesErrorPDU() {}
    public CmsGetLogStatusValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetLogStatusValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogStatusValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetLogStatusValuesErrorPDU", enc, data);
            CmsGetLogStatusValuesErrorPDU r = new CmsGetLogStatusValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
