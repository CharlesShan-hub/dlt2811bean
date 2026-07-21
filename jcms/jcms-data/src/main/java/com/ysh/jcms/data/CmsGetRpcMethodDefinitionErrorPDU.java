// Auto-generated. ASN.1 type: GetRpcMethodDefinitionErrorPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcMethodDefinition-ErrorPDU ::= ServiceError
 * }</pre>
 */
@Data
public class CmsGetRpcMethodDefinitionErrorPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int value;
    public CmsGetRpcMethodDefinitionErrorPDU() {}
    public CmsGetRpcMethodDefinitionErrorPDU(int value) { this.value = value; }
    public byte[] encode(String enc) {
        return CmsNative.encode("GetRpcMethodDefinitionErrorPDU", enc, String.valueOf(this.value));
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcMethodDefinitionErrorPDU decode(String enc, byte[] data) {
        try {
            String json = CmsNative.decode("GetRpcMethodDefinitionErrorPDU", enc, data);
            CmsGetRpcMethodDefinitionErrorPDU r = new CmsGetRpcMethodDefinitionErrorPDU();
            r.value = MAPPER.readTree(json).get("value").asInt();
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
