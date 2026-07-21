// Auto-generated. ASN.1 type: Apdu

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Apdu ::= SEQUENCE {
 *     apch        Apch,
 *     asdu        OCTET STRING (SIZE(0..65531))
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsApdu extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsApch apch = null;
    @JsonProperty public byte[] asdu = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("Apdu", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsApdu decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("Apdu", enc, data), CmsApdu.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
