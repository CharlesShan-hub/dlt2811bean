// Auto-generated. ASN.1 type: SetURCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetURCBValues-RequestPDU ::= SEQUENCE {
 *     urcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         rptID       [1] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *         rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [5] IMPLICIT RcbOptFlds OPTIONAL,
 *         bufTm       [6] IMPLICIT Int32U OPTIONAL,
 *         trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [9] IMPLICIT Int32U OPTIONAL,
 *         gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *         resv        [13] IMPLICIT BOOLEAN OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetURCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetURCBValuesRequestPDUUrcb> urcb = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetURCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetURCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetURCBValuesRequestPDU", enc, data), CmsSetURCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
