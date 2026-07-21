// Auto-generated. ASN.1 type: SubReference

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SubReference ::= VisibleString (SIZE(0..129))
 * }</pre>
 */
@Data
public class CmsSubReference extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String value;
    public CmsSubReference() {}
    public CmsSubReference(String value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("SubReference", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSubReference decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("SubReference", enc, data);
            CmsSubReference r = new CmsSubReference();
            r.value = MAPPER.readTree(json).get("value").asText();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
