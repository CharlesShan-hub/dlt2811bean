// Auto-generated. ASN.1 type: GetRpcInterfaceDefinitionRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
 *     interface       [0] IMPLICIT VisibleString,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetRpcInterfaceDefinitionRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String _interface = null;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcInterfaceDefinitionRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDefinitionRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetRpcInterfaceDefinitionRequestPDU", enc, data), CmsGetRpcInterfaceDefinitionRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
