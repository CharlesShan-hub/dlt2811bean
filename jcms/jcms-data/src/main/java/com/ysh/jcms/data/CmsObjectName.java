// Auto-generated. ASN.1 type: ObjectName

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ObjectName ::= VisibleString (SIZE(0..64))
 * }</pre>
 */
@Data
public class CmsObjectName extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String value;
    public CmsObjectName() {}
    public CmsObjectName(String value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("ObjectName", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsObjectName decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("ObjectName", enc, data);
            CmsObjectName r = new CmsObjectName();
            r.value = MAPPER.readTree(json).get("value").asText();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
