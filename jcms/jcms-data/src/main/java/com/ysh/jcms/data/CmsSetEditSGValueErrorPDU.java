// Auto-generated. ASN.1 type: SetEditSGValueErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetEditSGValue-ErrorPDU ::= SEQUENCE {
 *     result  [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetEditSGValueErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<Integer> result = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetEditSGValueErrorPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetEditSGValueErrorPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetEditSGValueErrorPDU", enc, data), CmsSetEditSGValueErrorPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
