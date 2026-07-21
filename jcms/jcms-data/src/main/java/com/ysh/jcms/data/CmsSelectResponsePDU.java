// Auto-generated. ASN.1 type: SelectResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Select-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSelectResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SelectResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SelectResponsePDU", enc, data), CmsSelectResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
