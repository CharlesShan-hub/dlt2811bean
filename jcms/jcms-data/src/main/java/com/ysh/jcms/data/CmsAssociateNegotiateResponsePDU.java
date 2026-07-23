// Auto-generated. ASN.1 type: AssociateNegotiateResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * AssociateNegotiate-ResponsePDU ::= SEQUENCE {
 *     apduSize        [0] IMPLICIT Int16U,
 *     asduSize        [1] IMPLICIT Int32U,
 *     protocolVersion [2] IMPLICIT Int32U,
 *     modelVersion    [3] IMPLICIT VisibleString
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAssociateNegotiateResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int apdu_size = 0;
    @JsonProperty public int asdu_size = 0;
    @JsonProperty public int protocol_version = 0;
    @JsonProperty public String model_version = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AssociateNegotiateResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAssociateNegotiateResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AssociateNegotiateResponsePDU", enc, data), CmsAssociateNegotiateResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
