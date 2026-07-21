// Auto-generated. ASN.1 type: Int32

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int32  ::= INTEGER (-2147483648..2147483647)
 * }</pre>
 */
@Data
public class CmsInt32 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt32() {}
    public CmsInt32(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int32", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt32 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int32", enc, data);
            CmsInt32 r = new CmsInt32();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
