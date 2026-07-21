// Auto-generated. ASN.1 type: FunctionalConstraint

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * FunctionalConstraint ::= VisibleString (SIZE (2))
 * }</pre>
 */
@Data
public class CmsFunctionalConstraint extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String value;
    public CmsFunctionalConstraint() {}
    public CmsFunctionalConstraint(String value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("FunctionalConstraint", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsFunctionalConstraint decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("FunctionalConstraint", enc, data);
            CmsFunctionalConstraint r = new CmsFunctionalConstraint();
            r.value = json.trim();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
