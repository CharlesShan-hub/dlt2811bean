// Auto-generated. ASN.1 type: AnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * (inline type within GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {)
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int version = 0;
    @JsonProperty public int timeout = 0;
    @JsonProperty public CmsDataDefinition request = null;
    @JsonProperty public CmsDataDefinition response = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("AnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("AnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod", enc, data), CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
