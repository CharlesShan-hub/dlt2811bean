// Auto-generated. ASN.1 type: GetFileRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString (SIZE (0..255)),
 *     startPosition   [1] IMPLICIT Int32U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetFileRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String filename = null;
    @JsonProperty public int start_position = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetFileRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetFileRequestPDU", enc, data), CmsGetFileRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
