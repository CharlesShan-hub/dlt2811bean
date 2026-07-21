// Auto-generated. ASN.1 type: Int8U

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int8U ::= INTEGER (0..255)
 * }</pre>
 */
@Data
public class CmsInt8U extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt8U() {}
    public CmsInt8U(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int8U", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt8U decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int8U", enc, data);
            CmsInt8U r = new CmsInt8U();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
