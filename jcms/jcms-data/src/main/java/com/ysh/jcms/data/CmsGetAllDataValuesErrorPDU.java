// Auto-generated. ASN.1 type: GetAllDataValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllDataValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetAllDataValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetAllDataValuesErrorPDU() {}
    public CmsGetAllDataValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetAllDataValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetAllDataValuesErrorPDU", enc, data);
            CmsGetAllDataValuesErrorPDU r = new CmsGetAllDataValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
