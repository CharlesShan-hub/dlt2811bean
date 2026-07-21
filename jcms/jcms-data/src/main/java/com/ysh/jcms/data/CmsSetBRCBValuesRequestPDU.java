// Auto-generated. ASN.1 type: SetBRCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetBRCBValues-RequestPDU ::= SEQUENCE {
 *     brcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         rptID       [1] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *         rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [5] IMPLICIT RcbOptFlds OPTIONAL,
 *         bufTm       [6] IMPLICIT Int32U OPTIONAL,
 *         trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [9] IMPLICIT Int32U OPTIONAL,
 *         gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *         purgeBuf    [11] IMPLICIT BOOLEAN OPTIONAL,
 *         entryID     [12] IMPLICIT EntryID OPTIONAL,
 *         resvTms     [13] IMPLICIT Int16 OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetBRCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetBRCBValuesRequestPDUBrcb> brcb = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetBRCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetBRCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetBRCBValuesRequestPDU", enc, data), CmsSetBRCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
