// Auto-generated. ASN.1 type: AssociateResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Associate-ResponsePDU ::= SEQUENCE {
 *     associationId                  [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError                   [1] IMPLICIT ServiceError,
 *     authenticationParameter        [2] IMPLICIT SEQUENCE {
 *         signatureCertificate        [0] IMPLICIT OCTET STRING,
 *         signedTime                  [1] IMPLICIT UtcTime,
 *         signedValue                 [2] IMPLICIT OCTET STRING
 *     } OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsAssociateResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] association_id = null;
    @JsonProperty public int service_error = 0;
    @JsonProperty public CmsAssociateResponsePDUAuthenticationParameter authentication_parameter = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AssociateResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AssociateResponsePDU", enc, data), CmsAssociateResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
