// Auto-generated. ASN.1 type: GetLogicalDeviceDirectoryResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
 *     lnReference       [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetLogicalDeviceDirectoryResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public java.util.List<String> ln_reference = new java.util.ArrayList<>();
    @JsonProperty public boolean more_follows = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLogicalDeviceDirectoryResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogicalDeviceDirectoryResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLogicalDeviceDirectoryResponsePDU", enc, data), CmsGetLogicalDeviceDirectoryResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
