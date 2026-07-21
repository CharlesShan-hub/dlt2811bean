// Auto-generated. ASN.1 type: SetEditSGValueRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetEditSGValue-RequestPDU ::= SEQUENCE {
 *     data    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         value       [2] IMPLICIT Data
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetEditSGValueRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetEditSGValueRequestPDUData> data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetEditSGValueRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetEditSGValueRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetEditSGValueRequestPDU", enc, data), CmsSetEditSGValueRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
