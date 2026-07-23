// Auto-generated. ASN.1 type: Int16

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int16 ::= INTEGER (-32768..32767)
 * }</pre>
 */
@Data
public class CmsInt16 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsInt16() {}
    public CmsInt16(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int16", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt16 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int16", enc, data);
            CmsInt16 r = new CmsInt16();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
