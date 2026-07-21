// Auto-generated. ASN.1 type: BinaryTime

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * BinaryTime ::= OCTET STRING (SIZE(6))
 * }</pre>
 */
@Data
public class CmsBinaryTime extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsBinaryTime() {}
    public CmsBinaryTime(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("BinaryTime", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsBinaryTime decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("BinaryTime", enc, data);
            CmsBinaryTime r = new CmsBinaryTime();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
