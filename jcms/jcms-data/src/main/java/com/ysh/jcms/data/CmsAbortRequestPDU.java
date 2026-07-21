// Auto-generated. ASN.1 type: AbortRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Abort-RequestPDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     reason           [1] IMPLICIT INTEGER {
 *         other                  (0),
 *         unrecognized-service   (1),
 *         invalid-reqID          (2),
 *         invalid-argument       (3),
 *         invalid-result         (4),
 *         max-serv-outstanding-exceeded (5)
 *     } (0..5)
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAbortRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] association_id = null;
    @JsonProperty public int reason = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AbortRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAbortRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AbortRequestPDU", enc, data), CmsAbortRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
