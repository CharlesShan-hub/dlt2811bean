// Auto-generated. ASN.1 type: ReleaseResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Release-ResponsePDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError     [1] IMPLICIT ServiceError
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsReleaseResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] association_id = null;
    @JsonProperty public int service_error = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("ReleaseResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsReleaseResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("ReleaseResponsePDU", enc, data), CmsReleaseResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
