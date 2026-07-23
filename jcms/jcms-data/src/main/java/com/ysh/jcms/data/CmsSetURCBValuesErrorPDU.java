// Auto-generated. ASN.1 type: SetURCBValuesErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetURCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         rptID       [1] IMPLICIT ServiceError OPTIONAL,
 *         rptEna      [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [5] IMPLICIT ServiceError OPTIONAL,
 *         bufTm       [6] IMPLICIT ServiceError OPTIONAL,
 *         trgOps      [8] IMPLICIT ServiceError OPTIONAL,
 *         intgPd      [9] IMPLICIT ServiceError OPTIONAL,
 *         gi          [10] IMPLICIT ServiceError OPTIONAL,
 *         resv        [13] IMPLICIT ServiceError OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetURCBValuesErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetURCBValuesErrorPDUResult> result = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetURCBValuesErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetURCBValuesErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetURCBValuesErrorPDU", enc, data), CmsSetURCBValuesErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
