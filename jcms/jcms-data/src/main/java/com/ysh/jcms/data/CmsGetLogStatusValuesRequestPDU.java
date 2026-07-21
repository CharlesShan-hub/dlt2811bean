// Auto-generated. ASN.1 type: GetLogStatusValuesRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogStatusValues-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetLogStatusValuesRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<String> log_reference = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLogStatusValuesRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogStatusValuesRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLogStatusValuesRequestPDU", enc, data), CmsGetLogStatusValuesRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
