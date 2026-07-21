// Auto-generated. ASN.1 type: Tcmd

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Tcmd ::= BIT STRING (SIZE(2))
 * }</pre>
 */
@Data
public class CmsTcmd extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsTcmd() {}
    public CmsTcmd(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("Tcmd", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsTcmd decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("Tcmd", enc, data);
            CmsTcmd r = new CmsTcmd();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
