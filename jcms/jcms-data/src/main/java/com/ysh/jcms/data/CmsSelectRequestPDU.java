// Auto-generated. ASN.1 type: SelectRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Select-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSelectRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SelectRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSelectRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SelectRequestPDU", enc, data), CmsSelectRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
