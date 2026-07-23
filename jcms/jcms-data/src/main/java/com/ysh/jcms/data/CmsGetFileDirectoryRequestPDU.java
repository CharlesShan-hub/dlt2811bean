// Auto-generated. ASN.1 type: GetFileDirectoryRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFileDirectory-RequestPDU ::= SEQUENCE {
 *     pathName        [0] IMPLICIT VisibleString (SIZE (0..255)),
 *     startTime       [1] IMPLICIT TimeStamp OPTIONAL,
 *     stopTime        [2] IMPLICIT TimeStamp OPTIONAL,
 *     fileAfter       [3] IMPLICIT VisibleString (SIZE (0..255)) OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetFileDirectoryRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String path_name = null;
    @JsonProperty public byte[] start_time = null;
    @JsonProperty public byte[] stop_time = null;
    @JsonProperty public String file_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetFileDirectoryRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileDirectoryRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetFileDirectoryRequestPDU", enc, data), CmsGetFileDirectoryRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
