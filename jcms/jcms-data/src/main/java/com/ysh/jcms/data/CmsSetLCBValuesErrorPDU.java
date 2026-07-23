// Auto-generated. ASN.1 type: SetLCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetLCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         logEna      [1] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [2] IMPLICIT ServiceError OPTIONAL,
 *         trgOps      [3] IMPLICIT ServiceError OPTIONAL,
 *         intgPd      [4] IMPLICIT ServiceError OPTIONAL,
 *         logRef      [5] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [6] IMPLICIT ServiceError OPTIONAL,
 *         bufTm       [7] IMPLICIT ServiceError OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetLCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetLCBValuesErrorPDUResult> result = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetLCBValuesErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetLCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetLCBValuesErrorPDU", enc, data), CmsSetLCBValuesErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
