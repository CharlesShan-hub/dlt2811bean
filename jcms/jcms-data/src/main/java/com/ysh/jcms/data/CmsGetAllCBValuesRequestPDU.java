// Auto-generated. ASN.1 type: GetAllCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetAllCBValues-RequestPDU ::= SEQUENCE {
 *     reference        [0] IMPLICIT CHOICE {
 *         ldName         [0] IMPLICIT ObjectName,
 *         lnReference    [1] IMPLICIT ObjectReference
 *     },
 *     acsiClass        [1] IMPLICIT ACSIClass,
 *     referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetAllCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsGetAllCBValuesRequestPDUReference reference = null;
    @JsonProperty public int acsi_class = 0;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetAllCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetAllCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetAllCBValuesRequestPDU", enc, data), CmsGetAllCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
