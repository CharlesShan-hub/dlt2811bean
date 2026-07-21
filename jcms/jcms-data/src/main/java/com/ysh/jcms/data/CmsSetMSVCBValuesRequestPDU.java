// Auto-generated. ASN.1 type: SetMSVCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetMSVCBValues-RequestPDU ::= SEQUENCE {
 *     msvcb           [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         svEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *         msvID       [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *         smpMod      [5] IMPLICIT SmpMod OPTIONAL,
 *         smpRate     [6] IMPLICIT Int16U OPTIONAL,
 *         optFlds     [7] IMPLICIT MsvcbOptFlds OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetMSVCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetMSVCBValuesRequestPDUMsvcb> msvcb = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetMSVCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetMSVCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetMSVCBValuesRequestPDU", enc, data), CmsSetMSVCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
