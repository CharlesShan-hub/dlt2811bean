// Auto-generated. ASN.1 type: Quality

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Quality ::= BIT STRING (SIZE(13))
 * }</pre>
 */
@Data
public class CmsQuality extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsQuality() {}
    public CmsQuality(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Quality", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsQuality decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Quality", enc, data);
            CmsQuality r = new CmsQuality();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
