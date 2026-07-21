// Auto-generated. ASN.1 type: GetAllDataDefinitionRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE {
 *     reference        [0] IMPLICIT CHOICE {
 *         ldName         [0] IMPLICIT ObjectName,
 *         lnReference    [1] IMPLICIT ObjectReference
 *     },
 *     fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetAllDataDefinitionRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsGetAllDataDefinitionRequestPDUReference reference = null;
    @JsonProperty public String fc = null;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataDefinitionRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataDefinitionRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllDataDefinitionRequestPDU", enc, data), CmsGetAllDataDefinitionRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
