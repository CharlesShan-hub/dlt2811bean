// Auto-generated. ASN.1 type: ConfirmEditSGValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
 *     sgcbReference       [0] IMPLICIT ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsConfirmEditSGValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String sgcb_reference = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ConfirmEditSGValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsConfirmEditSGValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ConfirmEditSGValuesRequestPDU", enc, data), CmsConfirmEditSGValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
