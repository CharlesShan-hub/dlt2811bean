// Auto-generated. ASN.1 type: GetDataValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetDataValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetDataValuesErrorPDU() {}
    public CmsGetDataValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetDataValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataValuesErrorPDU", enc, data);
            CmsGetDataValuesErrorPDU r = new CmsGetDataValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
