// Auto-generated. ASN.1 type: GetMSVCBValuesResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetMSVCBValues-ResponsePDU ::= SEQUENCE {
 *     msvcb           [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT MSVCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetMSVCBValuesResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetMSVCBValuesResponsePDUMsvcb> msvcb = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetMSVCBValuesResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetMSVCBValuesResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetMSVCBValuesResponsePDU", enc, data), CmsGetMSVCBValuesResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
