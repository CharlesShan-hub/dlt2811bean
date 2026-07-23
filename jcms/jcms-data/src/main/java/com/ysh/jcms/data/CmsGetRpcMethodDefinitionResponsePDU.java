// Auto-generated. ASN.1 type: GetRpcMethodDefinitionResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         method      [1] IMPLICIT SEQUENCE {
 *             version     [0] IMPLICIT Int32U,
 *             timeout     [1] IMPLICIT Int32U,
 *             request     [2] IMPLICIT DataDefinition,
 *             response    [3] IMPLICIT DataDefinition
 *         }
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetRpcMethodDefinitionResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<CmsAnonymousGetRpcMethodDefinitionResponsePDUReference> reference = null;
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcMethodDefinitionResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcMethodDefinitionResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetRpcMethodDefinitionResponsePDU", enc, data), CmsGetRpcMethodDefinitionResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
