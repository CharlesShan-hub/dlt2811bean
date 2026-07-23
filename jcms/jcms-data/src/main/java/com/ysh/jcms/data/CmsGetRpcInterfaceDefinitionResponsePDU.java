// Auto-generated. ASN.1 type: GetRpcInterfaceDefinitionResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {
 *     method          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         name        [0] IMPLICIT VisibleString,
 *         version     [1] IMPLICIT Int32U,
 *         timeout     [2] IMPLICIT Int32U,
 *         request     [3] IMPLICIT DataDefinition,
 *         response    [4] IMPLICIT DataDefinition
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetRpcInterfaceDefinitionResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod> method = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcInterfaceDefinitionResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDefinitionResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetRpcInterfaceDefinitionResponsePDU", enc, data), CmsGetRpcInterfaceDefinitionResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
