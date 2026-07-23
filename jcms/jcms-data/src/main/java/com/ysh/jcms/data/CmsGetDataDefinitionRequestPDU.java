// Auto-generated. ASN.1 type: GetDataDefinitionRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDefinition-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetDataDefinitionRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetDataDefinitionRequestPDUData> data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDefinitionRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDefinitionRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetDataDefinitionRequestPDU", enc, data), CmsGetDataDefinitionRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
