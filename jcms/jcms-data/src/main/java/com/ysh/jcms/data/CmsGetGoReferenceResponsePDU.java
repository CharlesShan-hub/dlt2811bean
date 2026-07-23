// Auto-generated. ASN.1 type: GetGoReferenceResponsePDU

package com.ysh.jcms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

/**
 * <pre>{@code
 * GetGoReference-ResponsePDU ::= SEQUENCE {
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT Int32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberData      [3] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         fc          [1] IMPLICIT FunctionalConstraint
 *     }
 * }
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CmsGetGoReferenceResponsePDU extends CmsBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @JsonProperty public String gocb_reference = null;
    @JsonProperty public int conf_rev = 0;
    @JsonProperty public String dat_set = null;
    @JsonProperty public java.util.List<CmsAnonymousGetGoReferenceResponsePDUMemberData> member_data = null;
    public byte[] encode(String enc) {
        try {
            return CmsNative.encode("GetGoReferenceResponsePDU", enc,
                MAPPER.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] encode() {
        return encode(DEFAULT_ENCODING);
    }
    public static CmsGetGoReferenceResponsePDU decode(String enc, byte[] data) {
        try {
            return MAPPER.readValue(CmsNative.decode("GetGoReferenceResponsePDU", enc, data), CmsGetGoReferenceResponsePDU.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
