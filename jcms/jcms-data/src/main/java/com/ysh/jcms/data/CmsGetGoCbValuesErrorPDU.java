// Auto-generated. ASN.1 type: GetGoCbValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGoCbValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetGoCbValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetGoCbValuesErrorPDU() {}
    public CmsGetGoCbValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetGoCbValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoCbValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGoCbValuesErrorPDU", enc, data);
            CmsGetGoCbValuesErrorPDU r = new CmsGetGoCbValuesErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
