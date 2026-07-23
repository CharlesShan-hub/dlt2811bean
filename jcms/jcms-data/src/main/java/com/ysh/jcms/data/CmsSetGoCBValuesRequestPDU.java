// Auto-generated. ASN.1 type: SetGoCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetGoCBValues-RequestPDU ::= SEQUENCE {
 *     gocb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         goEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *         goID        [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetGoCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetGoCBValuesRequestPDUGocb> gocb = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetGoCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetGoCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetGoCBValuesRequestPDU", enc, data), CmsSetGoCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
