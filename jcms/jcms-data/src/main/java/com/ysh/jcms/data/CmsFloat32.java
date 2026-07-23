// Auto-generated. ASN.1 type: Float32

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Float32 ::= OCTET STRING (SIZE (4))
 * }</pre>
 */
@Data
public class CmsFloat32 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsFloat32() {}
    public CmsFloat32(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Float32", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsFloat32 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Float32", enc, data);
            CmsFloat32 r = new CmsFloat32();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
