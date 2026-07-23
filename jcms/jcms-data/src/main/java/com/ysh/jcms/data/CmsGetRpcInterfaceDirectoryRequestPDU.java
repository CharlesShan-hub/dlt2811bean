// Auto-generated. ASN.1 type: GetRpcInterfaceDirectoryRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
 *     referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetRpcInterfaceDirectoryRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcInterfaceDirectoryRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDirectoryRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetRpcInterfaceDirectoryRequestPDU", enc, data), CmsGetRpcInterfaceDirectoryRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
