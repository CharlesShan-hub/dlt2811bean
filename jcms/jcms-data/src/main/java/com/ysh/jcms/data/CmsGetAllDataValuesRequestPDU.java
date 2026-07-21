// Auto-generated. ASN.1 type: GetAllDataValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllDataValues-RequestPDU ::= SEQUENCE {
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
public class CmsGetAllDataValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsGetAllDataValuesRequestPDUReference reference = null;
    @JsonProperty public String fc = null;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllDataValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllDataValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllDataValuesRequestPDU", enc, data), CmsGetAllDataValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
