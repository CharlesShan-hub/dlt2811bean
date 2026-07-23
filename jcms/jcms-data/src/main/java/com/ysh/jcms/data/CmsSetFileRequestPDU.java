// Auto-generated. ASN.1 type: SetFileRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * SetFile-RequestPDU ::= SEQUENCE {
 *     filename        [0] IMPLICIT VisibleString (SIZE (0..255)),
 *     startPosition   [1] IMPLICIT Int32U,
 *     fileData        [2] IMPLICIT OCTET STRING,
 *     endOfFile       [3] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsSetFileRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String filename = null;
    @JsonProperty public int start_position = 0;
    @JsonProperty public byte[] file_data = null;
    @JsonProperty public boolean end_of_file = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("SetFileRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsSetFileRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("SetFileRequestPDU", enc, data), CmsSetFileRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
