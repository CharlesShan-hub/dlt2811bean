// Auto-generated. ASN.1 type: Check

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Check ::= BIT STRING {
 *     syncheck          (0),
 *     interlock-check   (1)
 * } (SIZE (2))
 * }</pre>
 */
@Data
public class CmsCheck extends CmsBase {
    public static final int SYNCHECK = 0;
    public static final int INTERLOCK_CHECK = 1;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsCheck() {}
    public CmsCheck(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Check", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsCheck decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Check", enc, data);
            CmsCheck r = new CmsCheck();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
