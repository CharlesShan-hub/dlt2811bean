// Auto-generated. ASN.1 type: GetDataDirectoryRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetDataDirectory-RequestPDU ::= SEQUENCE {
 *     dataReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CmsGetDataDirectoryRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String data_reference = null;
    @JsonProperty public String reference_after = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetDataDirectoryRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetDataDirectoryRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetDataDirectoryRequestPDU", enc, data), CmsGetDataDirectoryRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
