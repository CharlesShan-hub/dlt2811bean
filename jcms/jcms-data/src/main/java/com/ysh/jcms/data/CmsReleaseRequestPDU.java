// Auto-generated. ASN.1 type: ReleaseRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Release-RequestPDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64))
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsReleaseRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] association_id = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ReleaseRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsReleaseRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ReleaseRequestPDU", enc, data), CmsReleaseRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
