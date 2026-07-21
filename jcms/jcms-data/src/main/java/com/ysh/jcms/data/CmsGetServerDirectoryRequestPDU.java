// Auto-generated. ASN.1 type: GetServerDirectoryRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetServerDirectory-RequestPDU ::= SEQUENCE {
 *     objectClass      [0] IMPLICIT INTEGER {
 *         reserved        (0),
 *         logical-device  (1),
 *         file-system     (2)
 *     } (0..2),
 *     referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetServerDirectoryRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public int object_class = 0;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetServerDirectoryRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetServerDirectoryRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetServerDirectoryRequestPDU", enc, data), CmsGetServerDirectoryRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
