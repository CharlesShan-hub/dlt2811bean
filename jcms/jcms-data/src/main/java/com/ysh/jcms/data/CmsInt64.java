// Auto-generated. ASN.1 type: Int64

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Int64  ::= INTEGER (-9223372036854775808..9223372036854775807)
 * }</pre>
 */
@Data
public class CmsInt64 extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public long value;
    public CmsInt64() {}
    public CmsInt64(long value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Int64", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsInt64 decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Int64", enc, data);
            CmsInt64 r = new CmsInt64();
            r.value = MAPPER.readTree(json).get("value").asLong();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
