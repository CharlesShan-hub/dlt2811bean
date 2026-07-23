// Auto-generated. ASN.1 type: AssociateRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * Associate-RequestPDU ::= SEQUENCE {
 *     serverAccessPointReference    [0] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *     authenticationParameter       [1] IMPLICIT SEQUENCE {
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
public class CmsAssociateRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String server_access_point_reference = null;
    @JsonProperty public CmsAssociateRequestPDUAuthenticationParameter authentication_parameter = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AssociateRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AssociateRequestPDU", enc, data), CmsAssociateRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
