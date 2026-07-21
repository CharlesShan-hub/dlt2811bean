// Auto-generated. ASN.1 type: GetLogicalDeviceDirectoryRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {
 *     ldName            [0] IMPLICIT ObjectName OPTIONAL,
 *     referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetLogicalDeviceDirectoryRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String ld_name = null;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetLogicalDeviceDirectoryRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetLogicalDeviceDirectoryRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetLogicalDeviceDirectoryRequestPDU", enc, data), CmsGetLogicalDeviceDirectoryRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
