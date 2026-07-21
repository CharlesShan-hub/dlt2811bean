// Auto-generated. ASN.1 type: GetDataSetValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataSetValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetDataSetValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetDataSetValuesErrorPDU() {}
    public CmsGetDataSetValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetDataSetValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataSetValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetDataSetValuesErrorPDU", enc, data);
            CmsGetDataSetValuesErrorPDU r = new CmsGetDataSetValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
