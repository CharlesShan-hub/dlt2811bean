// Auto-generated. ASN.1 type: SetMSVCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetMSVCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         svEna       [1] IMPLICIT ServiceError OPTIONAL,
 *         msvID       [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *         smpMod      [5] IMPLICIT ServiceError OPTIONAL,
 *         smpRate     [6] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [7] IMPLICIT ServiceError OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetMSVCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetMSVCBValuesErrorPDUResult> result = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetMSVCBValuesErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetMSVCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetMSVCBValuesErrorPDU", enc, data), CmsSetMSVCBValuesErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
