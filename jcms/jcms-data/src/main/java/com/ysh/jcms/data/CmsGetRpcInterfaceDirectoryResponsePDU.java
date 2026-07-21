// Auto-generated. ASN.1 type: GetRpcInterfaceDirectoryResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetRpcInterfaceDirectoryResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<String> reference = new java.util.ArrayList<>();
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetRpcInterfaceDirectoryResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetRpcInterfaceDirectoryResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetRpcInterfaceDirectoryResponsePDU", enc, data), CmsGetRpcInterfaceDirectoryResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
