// Auto-generated. ASN.1 type: Int8

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int8  ::= INTEGER (-128..127)
 * }</pre>
 */
@Data
public class CmsInt8 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt8() {}
    public CmsInt8(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int8", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt8 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int8", enc, data);
            CmsInt8 r = new CmsInt8();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
