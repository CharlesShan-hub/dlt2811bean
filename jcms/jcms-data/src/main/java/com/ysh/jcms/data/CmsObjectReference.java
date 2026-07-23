// Auto-generated. ASN.1 type: ObjectReference

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ObjectReference ::= VisibleString (SIZE(0..129))
 * }</pre>
 */
@Data
public class CmsObjectReference extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String value;
    public CmsObjectReference() {}
    public CmsObjectReference(String value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ObjectReference", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsObjectReference decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ObjectReference", enc, data);
            CmsObjectReference r = new CmsObjectReference();
            r.value = MAPPER.readTree(json).get("value").asText();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
