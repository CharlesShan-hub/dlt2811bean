// Auto-generated. ASN.1 type: Float64

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Float64 ::= OCTET STRING (SIZE (8))
 * }</pre>
 */
@Data
public class CmsFloat64 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsFloat64() {}
    public CmsFloat64(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Float64", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsFloat64 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Float64", enc, data);
            CmsFloat64 r = new CmsFloat64();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
