// Auto-generated. ASN.1 type: Int64U

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int64U ::= INTEGER (0..18446744073709551615)
 * }</pre>
 */
@Data
public class CmsInt64U extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public long value;
    public CmsInt64U() {}
    public CmsInt64U(long value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int64U", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt64U decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int64U", enc, data);
            CmsInt64U r = new CmsInt64U();
            r.value = MAPPER.readTree(json).get("value").asLong();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
