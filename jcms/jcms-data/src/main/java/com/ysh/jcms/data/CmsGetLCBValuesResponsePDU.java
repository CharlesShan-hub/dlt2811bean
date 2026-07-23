// Auto-generated. ASN.1 type: GetLCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLCBValues-ResponsePDU ::= SEQUENCE {
 *     lcb             [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT LCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetLCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetLCBValuesResponsePDULcb> lcb = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLCBValuesResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLCBValuesResponsePDU", enc, data), CmsGetLCBValuesResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
