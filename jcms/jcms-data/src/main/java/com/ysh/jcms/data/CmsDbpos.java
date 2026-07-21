// Auto-generated. ASN.1 type: Dbpos

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Dbpos ::= BIT STRING (SIZE(2))
 * }</pre>
 */
@Data
public class CmsDbpos extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsDbpos() {}
    public CmsDbpos(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Dbpos", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDbpos decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Dbpos", enc, data);
            CmsDbpos r = new CmsDbpos();
            r.value = Integer.parseInt(json.trim());
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
