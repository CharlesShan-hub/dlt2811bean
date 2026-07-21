// Auto-generated. ASN.1 type: Int32U

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int32U ::= INTEGER (0..4294967295)
 * }</pre>
 */
@Data
public class CmsInt32U extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt32U() {}
    public CmsInt32U(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int32U", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt32U decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int32U", enc, data);
            CmsInt32U r = new CmsInt32U();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
