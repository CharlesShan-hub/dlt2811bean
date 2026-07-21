// Auto-generated. ASN.1 type: GetGoReferenceErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGoReference-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetGoReferenceErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetGoReferenceErrorPDU() {}
    public CmsGetGoReferenceErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetGoReferenceErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoReferenceErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetGoReferenceErrorPDU", enc, data);
            CmsGetGoReferenceErrorPDU r = new CmsGetGoReferenceErrorPDU();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
