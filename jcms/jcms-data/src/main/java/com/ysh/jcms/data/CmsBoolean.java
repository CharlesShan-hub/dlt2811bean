// Auto-generated. ASN.1 type: Boolean

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Boolean ::= INTEGER (0..1)
 * }</pre>
 */
@Data
public class CmsBoolean extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsBoolean() {}
    public CmsBoolean(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Boolean", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsBoolean decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Boolean", enc, data);
            CmsBoolean r = new CmsBoolean();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
