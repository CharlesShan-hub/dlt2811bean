// Auto-generated. ASN.1 type: SetLCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetLCBValues-RequestPDU ::= SEQUENCE {
 *     lcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         logEna      [1] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [2] IMPLICIT ObjectReference OPTIONAL,
 *         trgOps      [3] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [4] IMPLICIT Int32U OPTIONAL,
 *         logRef      [5] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [6] IMPLICIT LcbOptFlds OPTIONAL,
 *         bufTm       [7] IMPLICIT Int32U OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetLCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetLCBValuesRequestPDULcb> lcb = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetLCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetLCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetLCBValuesRequestPDU", enc, data), CmsSetLCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
