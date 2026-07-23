// Auto-generated. ASN.1 type: Int24U

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int24U ::= INTEGER (0..16777215)
 * }</pre>
 */
@Data
public class CmsInt24U extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt24U() {}
    public CmsInt24U(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int24U", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt24U decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int24U", enc, data);
            CmsInt24U r = new CmsInt24U();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
