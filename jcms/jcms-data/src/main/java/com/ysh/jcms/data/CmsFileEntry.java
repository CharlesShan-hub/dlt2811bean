// Auto-generated. ASN.1 type: FileEntry

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * FileEntry ::= SEQUENCE {
 *     fileName       [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     fileSize       [1] IMPLICIT Int32U,
 *     lastModified   [2] IMPLICIT UtcTime,
 *     checkSum       [3] IMPLICIT Int32U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsFileEntry extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String file_name = null;
    @JsonProperty public int file_size = 0;
    @JsonProperty public byte[] last_modified = null;
    @JsonProperty public int check_sum = 0;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("FileEntry", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsFileEntry decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("FileEntry", enc, data), CmsFileEntry.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
