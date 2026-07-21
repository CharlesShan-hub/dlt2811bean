// Auto-generated. ASN.1 type: UtcTime

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * UtcTime ::= OCTET STRING (SIZE(8))
 * }</pre>
 */
@Data
public class CmsUtcTime extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] value;
    public CmsUtcTime() {}
    public CmsUtcTime(byte[] value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("UtcTime", enc, java.util.Base64.getEncoder().encodeToString(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsUtcTime decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("UtcTime", enc, data);
            CmsUtcTime r = new CmsUtcTime();
            r.value = java.util.Base64.getDecoder().decode(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
