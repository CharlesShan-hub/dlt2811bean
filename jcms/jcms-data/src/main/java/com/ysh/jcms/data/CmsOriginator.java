// Auto-generated. ASN.1 type: Originator

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Originator ::= SEQUENCE {
 *     orCat        [0] IMPLICIT INTEGER {
 *         notSupported      (0),
 *         bayControl        (1),
 *         stationControl    (2),
 *         remoteControl     (3),
 *         automaticBay      (4),
 *         automaticStation  (5),
 *         automaticRemote   (6),
 *         maintenance       (7),
 *         process           (8)
 *     } (0..8),
 *     orIdent      [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsOriginator extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int or_cat = 0;
    @JsonProperty public byte[] or_ident = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("Originator", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsOriginator decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("Originator", enc, data), CmsOriginator.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
