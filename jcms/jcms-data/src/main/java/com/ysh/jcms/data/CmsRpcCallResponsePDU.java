// Auto-generated. ASN.1 type: RpcCallResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * RpcCall-ResponsePDU ::= SEQUENCE {
 *     rspData         [0] IMPLICIT Data,
 *     nextCallID      [1] IMPLICIT OCTET STRING OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsRpcCallResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public CmsData rsp_data = null;
    @JsonProperty public byte[] next_call_id = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("RpcCallResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsRpcCallResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("RpcCallResponsePDU", enc, data), CmsRpcCallResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
