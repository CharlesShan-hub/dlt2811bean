// Auto-generated. ASN.1 type: SetGoCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetGoCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         goEna       [1] IMPLICIT ServiceError OPTIONAL,
 *         goID        [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetGoCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetGoCBValuesErrorPDUResult> result = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetGoCBValuesErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetGoCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetGoCBValuesErrorPDU", enc, data), CmsSetGoCBValuesErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
