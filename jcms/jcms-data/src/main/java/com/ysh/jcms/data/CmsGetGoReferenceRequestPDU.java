// Auto-generated. ASN.1 type: GetGoReferenceRequestPDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGoReference-RequestPDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     memberOfs       [1] IMPLICIT SEQUENCE OF Int16U
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetGoReferenceRequestPDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String gocb_reference = null;
    @JsonProperty public java.util.List<Integer> member_ofs = new java.util.ArrayList<>();
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGoReferenceRequestPDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoReferenceRequestPDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetGoReferenceRequestPDU", enc, data), CmsGetGoReferenceRequestPDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
