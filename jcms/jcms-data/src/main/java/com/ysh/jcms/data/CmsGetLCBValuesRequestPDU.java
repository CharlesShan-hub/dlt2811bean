// Auto-generated. ASN.1 type: GetLCBValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLCBValues-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetLCBValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<String> reference = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLCBValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLCBValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLCBValuesRequestPDU", enc, data), CmsGetLCBValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
