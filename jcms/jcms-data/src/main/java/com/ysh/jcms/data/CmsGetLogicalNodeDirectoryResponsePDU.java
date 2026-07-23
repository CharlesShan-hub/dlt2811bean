// Auto-generated. ASN.1 type: GetLogicalNodeDirectoryResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetLogicalNodeDirectoryResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<String> reference = new java.util.ArrayList<>();
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLogicalNodeDirectoryResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogicalNodeDirectoryResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLogicalNodeDirectoryResponsePDU", enc, data), CmsGetLogicalNodeDirectoryResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
