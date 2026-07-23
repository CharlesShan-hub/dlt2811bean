// Auto-generated. ASN.1 type: DeleteFileRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * DeleteFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString (SIZE (0..255))
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsDeleteFileRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String filename = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("DeleteFileRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsDeleteFileRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("DeleteFileRequestPDU", enc, data), CmsDeleteFileRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
