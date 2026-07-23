// Auto-generated. ASN.1 type: GetEditSGValueRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetEditSGValue-RequestPDU ::= SEQUENCE {
 *     data    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         fc          [1] IMPLICIT FunctionalConstraint
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetEditSGValueRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetEditSGValueRequestPDUData> data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetEditSGValueRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetEditSGValueRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetEditSGValueRequestPDU", enc, data), CmsGetEditSGValueRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
