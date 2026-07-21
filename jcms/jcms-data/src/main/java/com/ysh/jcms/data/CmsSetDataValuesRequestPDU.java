// Auto-generated. ASN.1 type: SetDataValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetDataValues-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         value         [2] IMPLICIT Data
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetDataValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousSetDataValuesRequestPDUData> data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetDataValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetDataValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetDataValuesRequestPDU", enc, data), CmsSetDataValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
