// Auto-generated. ASN.1 type: GetURCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetURCBValues-ResponsePDU ::= SEQUENCE {
 *     urcb            [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT URCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetURCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetURCBValuesResponsePDUUrcb> urcb = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetURCBValuesResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetURCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetURCBValuesResponsePDU", enc, data), CmsGetURCBValuesResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
