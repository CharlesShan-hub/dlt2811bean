// Auto-generated. ASN.1 type: AssociateNegotiateRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * AssociateNegotiate-RequestPDU ::= SEQUENCE {
 *     apduSize        [0] IMPLICIT Int16U,
 *     asduSize        [1] IMPLICIT Int32U,
 *     protocolVersion [2] IMPLICIT Int32U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAssociateNegotiateRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int apdu_size = 0;
    @JsonProperty public int asdu_size = 0;
    @JsonProperty public int protocol_version = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AssociateNegotiateRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateNegotiateRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AssociateNegotiateRequestPDU", enc, data), CmsAssociateNegotiateRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
