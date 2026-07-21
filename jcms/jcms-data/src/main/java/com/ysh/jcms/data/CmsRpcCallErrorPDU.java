// Auto-generated. ASN.1 type: RpcCallErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * RpcCall-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsRpcCallErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsRpcCallErrorPDU() {}
    public CmsRpcCallErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("RpcCallErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsRpcCallErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("RpcCallErrorPDU", enc, data);
            CmsRpcCallErrorPDU r = new CmsRpcCallErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
