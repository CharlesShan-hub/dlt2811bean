// Auto-generated. ASN.1 type: GetFileAttributeValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFileAttributeValues-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetFileAttributeValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetFileAttributeValuesErrorPDU() {}
    public CmsGetFileAttributeValuesErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetFileAttributeValuesErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileAttributeValuesErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetFileAttributeValuesErrorPDU", enc, data);
            CmsGetFileAttributeValuesErrorPDU r = new CmsGetFileAttributeValuesErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
