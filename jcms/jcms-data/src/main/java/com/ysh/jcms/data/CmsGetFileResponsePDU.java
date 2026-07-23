// Auto-generated. ASN.1 type: GetFileResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetFile-ResponsePDU ::= SEQUENCE {
 *     fileData        [0] IMPLICIT OCTET STRING,
 *     endOfFile       [1] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetFileResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public byte[] file_data = null;
    @JsonProperty public boolean end_of_file = false;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetFileResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetFileResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetFileResponsePDU", enc, data), CmsGetFileResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
