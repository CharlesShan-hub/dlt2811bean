// Auto-generated. ASN.1 type: RpcCallRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * RpcCall-RequestPDU ::= SEQUENCE {
 *     method          [0] IMPLICIT VisibleString,
 *     req             [1] IMPLICIT CHOICE {
 *         reqData     [0] IMPLICIT Data,
 *         callID      [1] IMPLICIT OCTET STRING
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsRpcCallRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String method = null;
    @JsonProperty public CmsRpcCallRequestPDUReq req = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("RpcCallRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsRpcCallRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("RpcCallRequestPDU", enc, data), CmsRpcCallRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
